-- Inserção de registros na tabela de clientes
INSERT INTO tb_client (client_name, token) VALUES ('Cliente teste', '9BZaUFXFwE');
INSERT INTO tb_client (client_name, token) VALUES ('Cliente tetste 2', '9BZaUFXFwE');
INSERT INTO tb_client (client_name, token) VALUES ('Mineracão Florense', 'eaI99vHHbL')

-- Inserção de registros na tabela de estações de serviço
INSERT INTO tb_station (external_id, name, code, commercial_station, cnpj, client_id) VALUES (400, 'Posto Central', 'POS001', true, '12.345.678/0001-99', 1);

-- Inserção de registros nas tabelas de recursos humanos
INSERT INTO tb_driver (external_id, name, code, client_id) VALUES (200, 'João Silva', 'MOT001', 1);
INSERT INTO tb_attendant (external_id, name, code, active, client_id) VALUES (300, 'Carlos Pereira', 'AT001', true, 1);

-- Inserção de registros nas tabelas de equipamentos e suprimentos
INSERT INTO tb_tank (external_id, code, name, client_id) VALUES (500, 'TAN001', 'Tanque 01', 1);
INSERT INTO tb_pump (external_id, code, name, client_id) VALUES (600, 'BOM001', 'Bomba 01', 1);
INSERT INTO tb_fuel (external_id, description, code, client_id) VALUES (1, 'Diesel S-10', 'COMB', 1);
INSERT INTO tb_product (name, price, client_id) VALUES ('Óleo ultraflex', 10.0, 1);
INSERT INTO tb_product (name, price, client_id) VALUES ('Filtro ultraflex', 15.0, 1);
INSERT INTO tb_product (name, price, client_id) VALUES ('Disco de freio XX', 1200.0, 1);

-- Inserção de tarefas associadas a produtos específicos
INSERT INTO tb_task (name, product_id, component, client_id) VALUES ('trocar o óleo do motor', 1, 'motor', 1);
INSERT INTO tb_task (name, product_id, component, client_id) VALUES ('trocar filtro de óleo do motor', 2, 'motor', 1);
INSERT INTO tb_task (name, component, client_id) VALUES ('trocar pastilhas freio', 'freio', 1);
INSERT INTO tb_task (name, component, client_id) VALUES ('Limpar carburador', 'motor', 2);
INSERT INTO tb_task (name, component, client_id) VALUES ('Revisar Motor', 'motor', 2);
INSERT INTO tb_task (name, product_id, component, client_id) VALUES ('Trocar disco de freio', 3, 'motor', 2);

-- Inserção de registros na tabela de veículos
INSERT INTO tb_vehicle (external_id, code, name, license_plate, fleet, bypass, category, model, rfid, current_odometer, current_hourmeter, client_id) VALUES (1000, 'VH001', 'Truck A1', 'PLT1234', 'Fleet A', false, 'Heavy', 'VOLVO TRUCK S3', 'RFID123456A', 25000, 15000, 1);
INSERT INTO tb_vehicle (external_id, code, name, license_plate, fleet, bypass, category, model, rfid, current_odometer, current_hourmeter, client_id) VALUES (1001, 'VH002', 'Truck A2', 'PLT1235', 'Fleet A', false, 'Medium', 'VOLVO TRUCK S3', 'RFID123456B', 31000, 1800, 1);
INSERT INTO tb_vehicle (external_id, code, name, license_plate, fleet, bypass, category, model, rfid, current_odometer, current_hourmeter, client_id) VALUES (1002, 'VH003', 'Truck A3', 'PLT1236', 'Fleet B', false, 'Light', 'VOLVO TRUCK S3', 'RFID123456C', 24400, 800, 2);
INSERT INTO tb_vehicle (external_id, code, name, license_plate, fleet, bypass, category, model, rfid, current_odometer, current_hourmeter, client_id) VALUES (1003, 'VH004', 'Truck A4', 'PLT1237', 'Fleet C', true, 'Special', 'VOLVO TRUCK S3', 'RFID123456D', 32000, 1000, 2);
INSERT INTO tb_vehicle (external_id, code, name, license_plate, fleet, bypass, category, model, rfid, current_odometer, current_hourmeter, client_id) VALUES (1004, 'VH005', 'Truck A5', 'PLT1238', 'Fleet D', false, 'Heavy', 'VOLVO TRUCK S3', 'RFID123456E', 22000, 1300, 2);
INSERT INTO tb_vehicle (external_id, code, name, license_plate, fleet, bypass, category, model, rfid, current_odometer, current_hourmeter, client_id) VALUES (1005, 'VH006', 'Truck A6', 'HLN5236', 'Fleet F', false, 'Heavy', 'VOLVO TRUCK S3', 'RFID123456E', 22000, 1300, 1);
 
 -- Inserção de registros da API token de teste da CTA
INSERT INTO tb_vehicle (external_id, name, fleet, license_plate, bypass, rfid, current_odometer,last_fueling_date, last_fueling_time, client_id) VALUES (55761, '9000', '9000', 'ABC9000', false, '0AC41507', 1,'2001-07-12', '00:30:00', 1);
INSERT INTO tb_vehicle (external_id, name, fleet,license_plate, bypass, current_odometer,last_fueling_date, last_fueling_time, client_id) VALUES (55763, '9002','9002', 'ABC9002', false, 50, '2001-07-12', '00:30:00',1);
INSERT INTO tb_vehicle (external_id, name, fleet, license_plate, bypass, current_odometer, last_fueling_date, last_fueling_time, client_id) VALUES (230610, 'não identificado','999999', 'ABC9999', true, 0,'2001-07-12', '00:30:00', 1);
INSERT INTO tb_vehicle (external_id, name, fleet, license_plate, bypass, current_odometer, last_fueling_date, last_fueling_time, client_id) VALUES (289367, '3000','3000', 'ABC3000', false, 3500,'2001-07-12', '00:30:00', 1);
INSERT INTO tb_vehicle (external_id, name, fleet, license_plate, bypass, rfid, current_odometer, last_fueling_date, last_fueling_time, client_id) VALUES (55761, '9000', '9000', 'ABC9000', false, '0AC41507', 1,'2001-07-12', '00:30:00', 2);
INSERT INTO tb_vehicle (external_id, name, fleet,license_plate, bypass, current_odometer,last_fueling_date, last_fueling_time, client_id) VALUES (55763, '9002','9002', 'ABC9002', false, 50,'2001-07-12', '00:30:00', 2);
INSERT INTO tb_vehicle (external_id, name, fleet, license_plate, bypass, current_odometer, last_fueling_date, last_fueling_time, client_id) VALUES (230610, 'não identificado','999999', 'ABC9999', true, 0,'2001-07-12', '00:30:00', 2);
INSERT INTO tb_vehicle (external_id, name, fleet, license_plate, bypass, current_odometer,last_fueling_date, last_fueling_time, client_id) VALUES (289367, '3000','3000', 'ABC3000', false, 3500,'2001-07-12', '00:30:00',2);

-- Inserção de planos de manutenção
INSERT INTO tb_plan (name, created_date, recurrence_interval, recurrence_type, tolerance, is_active, is_recurrent, client_id) VALUES ('Quarterly Inspection VOLVO S3', '2022-07-25', 90, 2, 10, true, true, 1);
INSERT INTO tb_plan (name, created_date, recurrence_interval, recurrence_type, tolerance, is_active, is_recurrent, client_id) VALUES  ('Annual Maintenance VOLVO S3', '2022-07-20', 365, 2, 15, true, true, 2);
INSERT INTO tb_plan (name, created_date, recurrence_interval, recurrence_type, tolerance, is_active, is_recurrent, client_id) VALUES  ('250KM VOLVO S3', '2022-07-25', 1500, 0, 500, true, true, 1);
INSERT INTO tb_plan (name, created_date, recurrence_interval, recurrence_type, tolerance, is_active, is_recurrent, client_id) VALUES  ('1000H VOLVO S3', '2022-07-20', 500, 0, 50, true, true, 2);
INSERT INTO tb_plan (name, created_date, recurrence_interval, recurrence_type, tolerance, is_active, is_recurrent, client_id) VALUES  ('Monthly Safety Check VOLVO S3', '2022-07-25', 30, 2, 5, true, true, 1);
INSERT INTO tb_plan (name, created_date, recurrence_interval, recurrence_type, tolerance, is_active, is_recurrent, client_id) VALUES ('10000H VOLVO S3', '2022-07-25', 10000, 1, 500, true, true, 1);
INSERT INTO tb_plan (name, created_date, recurrence_interval, recurrence_type, tolerance, is_active, is_recurrent, client_id) VALUES ('1000H VOLVO S3', '2022-07-25', 1000, 1, 200, true, true, 1);

-- Inserção de associações entre veículos e planos
INSERT INTO tb_plan_vehicle (plan_id, vehicle_id, last_service_odometer, next_service_odometer, status) VALUES (3, 1, 24000, 25500, 0);
INSERT INTO tb_plan_vehicle (plan_id, vehicle_id, last_service_odometer, next_service_odometer, status) VALUES (3, 2, 29500, 31000, 1);
INSERT INTO tb_plan_vehicle (plan_id, vehicle_id, last_service_odometer, next_service_odometer, status) VALUES (4, 3, 24000, 24500, 2);
INSERT INTO tb_plan_vehicle (plan_id, vehicle_id, last_service_odometer, next_service_odometer, status) VALUES (4, 4, 29500, 31000, 0);
INSERT INTO tb_plan_vehicle (plan_id, vehicle_id, last_service_hourmeter, next_service_hourmeter, status) VALUES ( 6, 1, 10800, 20800, 3);
INSERT INTO tb_plan_vehicle (plan_id, vehicle_id, last_service_hourmeter, next_service_hourmeter, status) VALUES ( 7, 2, 1000, 2000, 0);
INSERT INTO tb_plan_vehicle (plan_id, vehicle_id, last_service_date, next_service_date, status) VALUES ( 5, 1, '2024-03-25', '2024-04-24', 0);
INSERT INTO tb_plan_vehicle (plan_id, vehicle_id, last_service_date, next_service_date, status) VALUES ( 5, 2, '2024-04-25', '2024-05-24', 3);
INSERT INTO tb_plan_vehicle (plan_id, vehicle_id, last_service_date, next_service_date, status) VALUES ( 5, 6, '2024-04-05', '2024-05-04', 0);
INSERT INTO tb_plan_vehicle (plan_id, vehicle_id, last_service_odometer, next_service_odometer, status) VALUES ( 3, 7, 4, 1504, 0);
INSERT INTO tb_plan_vehicle (plan_id, vehicle_id, last_service_odometer, next_service_odometer, status) VALUES ( 3, 8, 50, 1550, 0);
INSERT INTO tb_plan_vehicle (plan_id, vehicle_id, last_service_date, next_service_date, status) VALUES ( 5, 9, '2024-04-05', '2024-05-05',0);
INSERT INTO tb_plan_vehicle (plan_id, vehicle_id, last_service_odometer, next_service_odometer, status) VALUES ( 3, 10, 2500, 4000, 0);

-- Inserão das associações entre planos e tarefas
INSERT INTO tb_plan_task (plan_id, task_id) VALUES (1, 1);
INSERT INTO tb_plan_task (plan_id, task_id) VALUES (1, 2);
INSERT INTO tb_plan_task (plan_id, task_id) VALUES (1, 3);
INSERT INTO tb_plan_task (plan_id, task_id) VALUES (3, 3);
INSERT INTO tb_plan_task (plan_id, task_id) VALUES (3, 1);
INSERT INTO tb_plan_task (plan_id, task_id) VALUES (2, 4);
INSERT INTO tb_plan_task (plan_id, task_id) VALUES (2, 5);
INSERT INTO tb_plan_task (plan_id, task_id) VALUES (2, 6);
INSERT INTO tb_plan_task (plan_id, task_id) VALUES (4, 4);
INSERT INTO tb_plan_task (plan_id, task_id) VALUES (4, 5);
INSERT INTO tb_plan_task (plan_id, task_id) VALUES (4, 6);

-- Insercao de oficina mecânica
INSERT INTO tb_garage (name, client_id) VALUES ('mecanica 1', 1);
INSERT INTO tb_garage (name, client_id) VALUES ('mecanica 2', 1);
INSERT INTO tb_garage (name, client_id) VALUES ('mecanica 3', 2);
INSERT INTO tb_garage (name, client_id) VALUES ('mecanica 4', 2);

-- Inserão de ordes de servico
INSERT INTO tb_service_order (created_date, conclusion_date, vehicle_id, plan_id, status, garage_id, client_id) VALUES ('2024-03-29', '2024-03-29', 1, 1, 0, 1, 1 );
INSERT INTO tb_service_order (created_date, conclusion_counter, vehicle_id, plan_id, status, garage_id, client_id) VALUES ('2024-03-29', 20000, 1, 2, 1, 2, 1 );
INSERT INTO tb_service_order (created_date, conclusion_counter, vehicle_id, plan_id, status, garage_id, client_id) VALUES ('2024-03-29', 10800, 1, 6, 0, 1, 1 );

-- Insercao de tarefas da ordem de servico
INSERT INTO tb_service_task  (task_id, service_id, is_completed, client_id ) VALUES (1, 1, true, 1);
INSERT INTO tb_service_task  (task_id, service_id, is_completed, client_id ) VALUES (2, 1, true, 1);
INSERT INTO tb_service_task  (task_id, service_id, is_completed, client_id ) VALUES (3, 1, true, 1);


-- Inserção de registros de usuários, papéis e associações de usuários com papéis
INSERT INTO tb_role (authority) VALUES ('ROLE_ADMIN');
INSERT INTO tb_user(first_name, last_name, email, password, client_id) VALUES ('Gaspar', 'Tartari', 'gaspar@gmail.com', '$2a$10$k0qK5Rnu8mGN09KrCRNRHOaI3VkvenhNnt7iXPERNumfDY/uTinoi', 1);
INSERT INTO tb_user(first_name, last_name, email, password, client_id) VALUES ('Joao', 'Pedro', 'joao@gmail.com', '$2a$10$k0qK5Rnu8mGN09KrCRNRHOaI3VkvenhNnt7iXPERNumfDY/uTinoi', 2);
INSERT INTO tb_user(first_name, last_name, email, password, client_id) VALUES ('Nicolas', 'Min', 'nicolas@gmail.com', '$2a$10$k0qK5Rnu8mGN09KrCRNRHOaI3VkvenhNnt7iXPERNumfDY/uTinoi', 3);
INSERT INTO tb_user_role (user_id, role_id) VALUES (1, 1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (2, 1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (3, 1);

-- Assegure-se de que todas as inserções respeitem as dependências de chave estrangeira
