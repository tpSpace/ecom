-- Sales by day
CREATE VIEW vw_sales_by_day AS
SELECT 
    DATE_TRUNC('day', o.created_at) AS day,
    COUNT(DISTINCT o.order_id) AS order_count,
    SUM(o.total) AS total_sales,
    AVG(o.total) AS average_order_value
FROM 
    orders o
WHERE 
    o.order_status NOT IN ('CANCELLED', 'REFUNDED')
GROUP BY 
    DATE_TRUNC('day', o.created_at)
ORDER BY 
    day DESC;

-- Top selling products
CREATE VIEW vw_top_selling_products AS
SELECT 
    p.product_id,
    p.name,
    SUM(oi.quantity) AS total_quantity_sold,
    SUM(oi.subtotal) AS total_revenue
FROM 
    products p
JOIN 
    order_items oi ON p.product_id = oi.product_id
JOIN 
    orders o ON oi.order_id = o.order_id
WHERE 
    o.order_status NOT IN ('CANCELLED', 'REFUNDED')
GROUP BY 
    p.product_id, p.name
ORDER BY 
    total_quantity_sold DESC;

-- Customer purchase frequency
CREATE VIEW vw_customer_purchase_frequency AS
SELECT 
    u.user_id,
    u.email,
    COUNT(DISTINCT o.order_id) AS order_count,
    SUM(o.total) AS total_spent,
    MIN(o.created_at) AS first_purchase,
    MAX(o.created_at) AS last_purchase,
    (EXTRACT(EPOCH FROM (MAX(o.created_at) - MIN(o.created_at))) / 86400) / 
        GREATEST(1, (COUNT(DISTINCT o.order_id) - 1)) AS avg_days_between_orders
FROM 
    users u
JOIN 
    orders o ON u.user_id = o.user_id
WHERE 
    o.order_status NOT IN ('CANCELLED', 'REFUNDED')
GROUP BY 
    u.user_id, u.email
ORDER BY 
    total_spent DESC;

-- Inventory status
CREATE VIEW vw_inventory_status AS
SELECT 
    p.product_id,
    p.name,
    p.sku,
    p.quantity AS current_stock,
    CASE 
        WHEN p.quantity <= 0 THEN 'OUT_OF_STOCK'
        WHEN p.quantity < 10 THEN 'LOW_STOCK'
        ELSE 'IN_STOCK'
    END AS stock_status,
    COALESCE(SUM(oi.quantity), 0) AS units_sold
FROM 
    products p
LEFT JOIN 
    order_items oi ON p.product_id = oi.product_id
LEFT JOIN 
    orders o ON oi.order_id = o.order_id AND o.order_status NOT IN ('CANCELLED', 'REFUNDED')
GROUP BY 
    p.product_id, p.name, p.sku, p.quantity
ORDER BY 
    current_stock ASC;

-- Payment method distribution
CREATE VIEW vw_payment_method_distribution AS
SELECT 
    payment_method,
    COUNT(*) AS payment_count,
    SUM(amount) AS total_amount,
    (COUNT(*) * 100.0 / (SELECT COUNT(*) FROM payments)) AS percentage
FROM 
    payments
WHERE 
    payment_status = 'COMPLETED'
GROUP BY 
    payment_method
ORDER BY 
    total_amount DESC;

-- Function to update product quantity after order
CREATE OR REPLACE FUNCTION update_product_quantity()
RETURNS TRIGGER AS $$
BEGIN
    -- Decrease product quantity
    UPDATE products
    SET quantity = quantity - NEW.quantity
    WHERE product_id = NEW.product_id;
    
    -- If variant exists, decrease variant quantity too
    IF NEW.variant_id IS NOT NULL THEN
        UPDATE product_variants
        SET quantity = quantity - NEW.quantity
        WHERE variant_id = NEW.variant_id;
    END IF;
    
    -- Create inventory transaction record
    INSERT INTO inventory_transactions (
        product_id, 
        variant_id, 
        quantity, 
        reference_type, 
        reference_id, 
        notes
    ) VALUES (
        NEW.product_id,
        NEW.variant_id,
        -NEW.quantity,
        'ORDER',
        NEW.order_id,
        'Order item creation'
    );
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger to update product quantity after order
CREATE TRIGGER trg_update_product_quantity
AFTER INSERT ON order_items
FOR EACH ROW
EXECUTE FUNCTION update_product_quantity();

-- Function to update timestamps
CREATE OR REPLACE FUNCTION update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Apply update_timestamp trigger to relevant tables
CREATE TRIGGER trg_update_timestamp_products
BEFORE UPDATE ON products
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

CREATE TRIGGER trg_update_timestamp_users
BEFORE UPDATE ON users
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

CREATE TRIGGER trg_update_timestamp_orders
BEFORE UPDATE ON orders
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- Function to calculate order totals
CREATE OR REPLACE FUNCTION calculate_order_total()
RETURNS TRIGGER AS $$
BEGIN
    -- Calculate subtotal from order items
    SELECT COALESCE(SUM(subtotal), 0) INTO NEW.subtotal
    FROM order_items
    WHERE order_id = NEW.order_id;
    
    -- Calculate total
    NEW.total = NEW.subtotal + NEW.tax + NEW.shipping_cost - NEW.discount;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger to calculate order totals
CREATE TRIGGER trg_calculate_order_total
BEFORE INSERT OR UPDATE ON orders
FOR EACH ROW
EXECUTE FUNCTION calculate_order_total();
