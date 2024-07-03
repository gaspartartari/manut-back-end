package com.manut.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.manut.api.entities.PlanVehicle;
import com.manut.api.entities.PlanVehiclePK;
import com.manut.api.projections.PlanVehicleProjection;

public interface PlanVehicleRepository extends JpaRepository<PlanVehicle, PlanVehiclePK> {

    @Query(nativeQuery = true, value = """
                SELECT * FROM tb_plan_vehicle
            """)
    List<PlanVehicle> searchAll();

    @Query(nativeQuery = true, value = """
                SELECT pv.plan_id AS planId, pv.vehicle_id AS vehicleId, v.external_id AS externalId, p.recurrence_type AS recurrenceType,
                v.current_hourmeter AS currentHourmeter, pv.last_service_hourmeter AS lastServiceHourmeter, pv.next_service_hourmeter AS nextServiceHourmeter,
                v.current_odometer AS currentOdometer, pv.last_service_odometer AS lastServiceOdometer, pv.next_service_odometer AS nextServiceOdometer,
                pv.last_service_date AS lastServiceDate, pv.next_service_date AS nextServiceDate,
                pv.status, p.is_recurrent AS isRecurrent, p.tolerance, p.recurrence_interval AS recurrenceInterval, p.client_id AS clientId, FROM TB_PLAN_VEHICLE pv
                INNER JOIN tb_plan p ON pv.plan_id = p.id
                INNER JOIN tb_vehicle v ON pv.vehicle_id = v.id
                WHERE v.external_id = :externalId
                AND p.client_id = :clientId
            """)
    List<PlanVehicleProjection> searchPlansByVehicleId(Long externalId, Long clientId);

    @Query(nativeQuery = true, value = """
                SELECT pv.plan_id AS planId, pv.vehicle_id AS vehicleId, v.external_id AS externalId, p.recurrence_type AS recurrenceType,
                v.current_hourmeter AS currentHourmeter, pv.last_service_hourmeter AS lastServiceHourmeter, pv.next_service_hourmeter AS nextServiceHourmeter,
                v.current_odometer AS currentOdometer, pv.last_service_odometer AS lastServiceOdometer, pv.next_service_odometer AS nextServiceOdometer,
                pv.last_service_date AS lastServiceDate, pv.next_service_date AS nextServiceDate,
                pv.status, p.is_recurrent AS isRecurrent, p.tolerance, p.recurrence_interval AS recurrenceInterval, p.client_id AS clientId, FROM TB_PLAN_VEHICLE pv
                INNER JOIN tb_plan p ON pv.plan_id = p.id
                INNER JOIN tb_vehicle v ON pv.vehicle_id = v.id
                AND p.client_id = :clientId
            """)
    List<PlanVehicleProjection> dashoardList(Long clientId);

    @Query("SELECT pv FROM PlanVehicle pv " +
            "JOIN FETCH pv.id.plan p " +
            "JOIN FETCH pv.id.vehicle v " +
            "WHERE p.client.id = :clientId OR v.client.id = :clientId")
    List<PlanVehicle> findPlanVehicleListByClientId(@Param("clientId") Long clientId);

}
