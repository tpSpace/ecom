package com.example.ecommerce.service;

import com.example.ecommerce.dto.ProductResponse;
import com.example.ecommerce.mapper.ProductMapper;
import com.example.ecommerce.model.ProductModel;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.vector_service.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Objects;

@Service
public class VectorSearchService {

    private final VectorServiceGrpc.VectorServiceBlockingStub blockingStub;
    private final ProductRepository productRepository;
    private final ProductMapper mapper;
    private final String grpcAddress;
    private final int grpcPort;

    @Autowired
    public VectorSearchService(ProductRepository productRepository, ProductMapper mapper,
                               @Value("${grpc.client.vector-service.address}") String grpcAddress,
                               @Value("${grpc.client.vector-service.port}") int grpcPort) {
        this.productRepository = productRepository;
        this.mapper = mapper;
        this.grpcAddress = grpcAddress;
        this.grpcPort = grpcPort;
        ManagedChannel channel = ManagedChannelBuilder.forAddress(this.grpcAddress, this.grpcPort)
                .usePlaintext()
                .build();
        this.blockingStub = VectorServiceGrpc.newBlockingStub(channel);
    }

    public String addProductEmbedding(UUID productId, String name, String description) {
        AddProductEmbeddingRequest request = AddProductEmbeddingRequest.newBuilder()
                .setProductId(productId.toString())
                .setName(name)
                .setDescription(description)
                .build();
        AddProductEmbeddingResponse response = blockingStub.addProductEmbedding(request);
        return response.getProductId();
    }

    public void deleteProductEmbedding(UUID productId) {
        DeleteProductEmbeddingRequest request = DeleteProductEmbeddingRequest.newBuilder()
                .setProductId(productId.toString())
                .build();
        DeleteProductEmbeddingResponse response = blockingStub.deleteProductEmbedding(request);
        if (!response.getSuccess()) {
            throw new RuntimeException("Failed to delete product embedding");
        }
    }

    public Page<ProductResponse> searchProducts(String query, int page, int size) {
        SearchProductsRequest request = SearchProductsRequest.newBuilder()
                .setTextQuery(query)
                .build();
        SearchProductsResponse response = blockingStub.searchProducts(request);
        List<ProductResult> results = response.getResultsList();

        List<UUID> productIds = results.stream()
                .map(result -> UUID.fromString(result.getId()))
                .collect(Collectors.toList());

        List<ProductModel> products = productRepository.findAllById(productIds);
        List<ProductModel> orderedProducts = productIds.stream()
                .map(id -> products.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<ProductResponse> productResponses = orderedProducts.stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());

        int start = Math.min(page * size, productResponses.size());
        int end = Math.min(start + size, productResponses.size());
        List<ProductResponse> pagedResponses = productResponses.subList(start, end);

        return new PageImpl<>(pagedResponses, PageRequest.of(page, size), productResponses.size());
    }
}