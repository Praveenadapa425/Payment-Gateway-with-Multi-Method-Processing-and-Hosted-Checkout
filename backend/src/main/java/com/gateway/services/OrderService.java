package com.gateway.services;

import com.gateway.dto.*;
import com.gateway.models.Merchant;
import com.gateway.models.Order;
import com.gateway.models.Payment;
import com.gateway.repositories.MerchantRepository;
import com.gateway.repositories.OrderRepository;
import com.gateway.repositories.PaymentRepository;
import com.gateway.utils.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    @Value("${TEST_MODE:false}")
    private boolean testMode;

    @Value("${TEST_PAYMENT_SUCCESS:true}")
    private boolean testPaymentSuccess;

    @Value("${PROCESSING_DELAY_MIN:5000}")
    private int processingDelayMin;

    @Value("${PROCESSING_DELAY_MAX:10000}")
    private int processingDelayMax;

    @Value("${TEST_PROCESSING_DELAY:1000}")
    private int testProcessingDelay;

    public CreateOrderResponse createOrder(String apiKey, String apiSecret, CreateOrderRequest request) {
        // Authenticate merchant
        Optional<Merchant> merchantOpt = merchantRepository.findByApiKeyAndApiSecret(apiKey, apiSecret);
        if (!merchantOpt.isPresent()) {
            throw new RuntimeException("Invalid API credentials");
        }

        Merchant merchant = merchantOpt.get();

        // Validate request
        if (request.getAmount() == null || request.getAmount() < 100) {
            throw new RuntimeException("amount must be at least 100");
        }

        String currency = request.getCurrency() != null ? request.getCurrency() : "INR";

        // Generate order ID
        String orderId = IdGenerator.generateOrderId();

        // Create order entity
        Order order = new Order();
        order.setId(orderId);
        order.setMerchantId(merchant.getId());
        order.setAmount(request.getAmount());
        order.setCurrency(currency);
        order.setReceipt(request.getReceipt());
        // Convert notes object to JSON string
        try {
            order.setNotes(request.getNotes() != null ? objectMapper.writeValueAsString(request.getNotes()) : null);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new RuntimeException("Error processing notes", e);
        }
        order.setStatus("created");

        // Save order
        order = orderRepository.save(order);

        // Create response
        CreateOrderResponse response = new CreateOrderResponse();
        response.setId(order.getId());
        response.setMerchantId(order.getMerchantId().toString());
        response.setAmount(order.getAmount());
        response.setCurrency(order.getCurrency());
        response.setReceipt(order.getReceipt());
        response.setNotes(order.getNotes());
        response.setStatus(order.getStatus());
        response.setCreatedAt(order.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));

        return response;
    }

    public GetOrderResponse getOrder(String apiKey, String apiSecret, String orderId) {
        // Authenticate merchant
        Optional<Merchant> merchantOpt = merchantRepository.findByApiKeyAndApiSecret(apiKey, apiSecret);
        if (!merchantOpt.isPresent()) {
            throw new RuntimeException("Invalid API credentials");
        }

        Merchant merchant = merchantOpt.get();

        // Find order by ID and merchant ID
        Optional<Order> orderOpt = orderRepository.findByIdAndMerchantId(orderId, merchant.getId());
        if (!orderOpt.isPresent()) {
            throw new RuntimeException("Order not found");
        }

        Order order = orderOpt.get();

        // Create response
        GetOrderResponse response = new GetOrderResponse();
        response.setId(order.getId());
        response.setMerchantId(order.getMerchantId().toString());
        response.setAmount(order.getAmount());
        response.setCurrency(order.getCurrency());
        response.setReceipt(order.getReceipt());
        response.setNotes(order.getNotes());
        response.setStatus(order.getStatus());
        response.setCreatedAt(order.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        response.setUpdatedAt(order.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));

        return response;
    }

    public List<GetOrderResponse> getAllOrders(String apiKey, String apiSecret) {
        // Authenticate merchant
        Optional<Merchant> merchantOpt = merchantRepository.findByApiKeyAndApiSecret(apiKey, apiSecret);
        if (!merchantOpt.isPresent()) {
            throw new RuntimeException("Invalid API credentials");
        }

        Merchant merchant = merchantOpt.get();

        // Find all orders for this merchant
        List<Order> orders = orderRepository.findByMerchantId(merchant.getId());

        // Convert to responses
        return orders.stream()
            .map(this::convertToGetOrderResponse)
            .collect(Collectors.toList());
    }

    private GetOrderResponse convertToGetOrderResponse(Order order) {
        GetOrderResponse response = new GetOrderResponse();
        response.setId(order.getId());
        response.setMerchantId(order.getMerchantId().toString());
        response.setAmount(order.getAmount());
        response.setCurrency(order.getCurrency());
        response.setReceipt(order.getReceipt());
        response.setNotes(order.getNotes());
        response.setStatus(order.getStatus());
        response.setCreatedAt(order.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        response.setUpdatedAt(order.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        return response;
    }

    private String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * characters.length());
            result.append(characters.charAt(index));
        }
        return result.toString();
    }
}