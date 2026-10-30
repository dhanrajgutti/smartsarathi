package com.vehical.dao;

import com.vehical.DatabaseConfig;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class PolicyDAO {

    public static boolean insertInsurancePolicy(
            String policyNumber,
            String vehicleNo,
            String ownerName,
            String ownerMobile,
            String ownerEmail,
            String vehicleType,
            String makeModel,
            String fuelType,
            String engineNumber,
            String chassisNumber,
            String policyCategory,
            String policyType,
            Double idv,
            Double premium,
            String startDate,
            String endDate,
            Long issuedByUserId,
            String issuedByName
    ) {
        String sql = "INSERT INTO insurance_policy " +
                "(policy_number, vehicle_no, owner_name, owner_mobile, owner_email, " +
                "vehicle_type, make_model, fuel_type, engine_number, chassis_number, " +
                "policy_category, policy_type, idv, premium, start_date, end_date, " +
                "status, issued_by_user_id, issued_by_name) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), TO_DATE(?, 'YYYY-MM-DD'), 'Issued', ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, policyNumber);
            ps.setString(2, vehicleNo);
            ps.setString(3, ownerName);
            ps.setString(4, ownerMobile);
            ps.setString(5, ownerEmail);
            ps.setString(6, vehicleType);
            ps.setString(7, makeModel);
            ps.setString(8, fuelType);
            ps.setString(9, engineNumber);
            ps.setString(10, chassisNumber);
            ps.setString(11, policyCategory);
            ps.setString(12, policyType);
            ps.setObject(13, idv);
            ps.setObject(14, premium);
            ps.setString(15, startDate);
            ps.setString(16, endDate);
            ps.setObject(17, issuedByUserId);
            ps.setString(18, issuedByName);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error inserting insurance policy: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateVehicleInsuranceStatus(String vehicleNo, String newStatus) {
        String sql = "UPDATE vehicle SET insurance_status = ?, updated_at = SYSTIMESTAMP WHERE vehicle_no = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newStatus);
            ps.setString(2, vehicleNo);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating vehicle insurance status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static List<Map<String, Object>> getAllInsurancePolicies() {
        List<Map<String, Object>> policies = new ArrayList<>();
        String sql = "SELECT policy_id, policy_number, vehicle_no, owner_name, owner_mobile, owner_email, " +
                "vehicle_type, make_model, fuel_type, engine_number, chassis_number, " +
                "policy_category, policy_type, idv, premium, start_date, end_date, status, " +
                "issued_by_name, created_at " +
                "FROM insurance_policy ORDER BY created_at DESC";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> policy = new HashMap<>();
                policy.put("policyId", rs.getLong("policy_id"));
                policy.put("policyNumber", rs.getString("policy_number"));
                policy.put("vehicleNo", rs.getString("vehicle_no"));
                policy.put("ownerName", rs.getString("owner_name"));
                policy.put("ownerMobile", rs.getString("owner_mobile"));
                policy.put("ownerEmail", rs.getString("owner_email"));
                policy.put("vehicleType", rs.getString("vehicle_type"));
                policy.put("makeModel", rs.getString("make_model"));
                policy.put("fuelType", rs.getString("fuel_type"));
                policy.put("engineNumber", rs.getString("engine_number"));
                policy.put("chassisNumber", rs.getString("chassis_number"));
                policy.put("policyCategory", rs.getString("policy_category"));
                policy.put("policyType", rs.getString("policy_type"));
                policy.put("idv", rs.getObject("idv"));
                policy.put("premium", rs.getObject("premium"));
                policy.put("startDate", rs.getDate("start_date") != null ? rs.getDate("start_date").toString() : null);
                policy.put("endDate", rs.getDate("end_date") != null ? rs.getDate("end_date").toString() : null);
                policy.put("status", rs.getString("status"));
                policy.put("issuedByName", rs.getString("issued_by_name"));
                policy.put("createdAt", rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toString() : null);
                policies.add(policy);
            }

        } catch (SQLException e) {
            System.err.println("Error fetching insurance policies: " + e.getMessage());
            e.printStackTrace();
        }

        return policies;
    }
}
