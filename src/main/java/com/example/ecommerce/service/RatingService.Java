package com.example.ecommerce.service;

@Service
public class RatingService {

    @Autowired
    private final RatingRepository ratingRepository;

    public List<Rating> getRatingByProductId(UUID id) {
        return ratingRepository.findByProductId(id);
    }

    public Rating getRatingByUserId(UUID id) {
        return ratingRepository.findByUserId(id);
    }


}