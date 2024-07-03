package com.manut.api.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.manut.api.entities.Plan;
import com.manut.api.projections.MainTableProjection;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    @Query("SELECT obj FROM Plan obj JOIN FETCH obj.client WHERE obj.client.id = :clientId")
    Page<Plan> findAllPlans(Long clientId, Pageable pageable);

    @Query(nativeQuery = true, value = """
            SELECT p.id AS planId, p.name AS planName, v.license_plate AS licensePlate, p.recurrence_type AS type, p.recurrence_interval AS recurrenceInterval, pv.last_service_date AS lastServiceDate,
            pv.next_service_date AS nextServiceDate, pv.last_service_odometer AS lastServiceOdomeder, pv.next_service_odometer AS nextServiceOdometer, pv.last_service_hourmeter AS lastServiceHourmeter,
            pv.next_service_hourmeter AS nextServiceHourmeter, v.current_odometer AS currentOdometer, v.current_hourmeter AS currentHourmeter, pv.status FROM tb_plan p
            INNER JOIN tb_plan_vehicle pv ON p.id = pv.plan_id
            INNER JOIN tb_vehicle v ON pv.vehicle_id = v.id
            WHERE p.client_id = :clientId AND (:planId IS NULL OR p.id = :planId)
            AND (:maintenanceStatus IS NULL OR pv.status = :maintenanceStatus)
            AND UPPER(v.license_plate) LIKE UPPER(CONCAT('%', :licensePlate, '%'))
                """, countQuery = """
            SELECT COUNT(*) FROM tb_plan p
            INNER JOIN tb_plan_vehicle pv ON p.id = pv.plan_id
            INNER JOIN tb_vehicle v ON pv.vehicle_id = v.id
            WHERE p.client_id = :clientId AND (:planId IS NULL OR p.id = :planId)
            AND (:maintenanceStatus IS NULL OR pv.status = :maintenanceStatus)
            AND UPPER(v.license_plate) LIKE UPPER(CONCAT('%', :licensePlate, '%'))
            """)
    Page<MainTableProjection> mainTable(Long clientId, Pageable pageable, Long planId, Long maintenanceStatus, String licensePlate);

}
