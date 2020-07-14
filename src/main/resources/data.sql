SET FOREIGN_KEY_CHECKS=0;

TRUNCATE TABLE CRM_USER;
TRUNCATE TABLE CRM_PERMISSION;
TRUNCATE TABLE CRM_ROLE;

SET FOREIGN_KEY_CHECKS=1;

INSERT INTO CRM_ROLE(id, name) VALUES ('a4a14179-881c-48db-9551-b490ba968e91', 'SUPER_USER');
INSERT INTO CRM_ROLE(id, name) VALUES ('a4a14179-881c-48db-9551-b490ba968e92', 'MANAGER');
INSERT INTO CRM_ROLE(id, name) VALUES ('a4a14179-881c-48db-9551-b490ba968e93', 'OPERATOR');

INSERT INTO CRM_USER(id, name, password, email, role_id) VALUES ('914fca00-63c4-42d5-8240-51f2ad6a0ce8', 'admin',
                                                                 '$2a$10$5V3ABxHadLb7rdPAz4oHIee81OeVj4PXlu.KdgNwZciP6f1Abgay2', 'admin@smartbee.com', 'a4a14179-881c-48db-9551-b490ba968e91');
INSERT INTO CRM_USER(id, name, password, email, role_id) VALUES ('914fca00-63c4-42d5-8240-51f2ad6a0ce9', 'manager',
                                                                 '$2y$12$TSRUPLx9gFoX1NOgQPhvU.o28rWuoOjN/jXyslAVR3Ii4uF1z1wzK', 'manager@smartbee.com', 'a4a14179-881c-48db-9551-b490ba968e92');
INSERT INTO CRM_USER(id, name, password, email, role_id) VALUES ('914fca00-63c4-42d5-8240-51f2ad6a0ce0', 'operator',
                                                                 '$2y$12$8t8EzJ0UzqAQUNr9rP2Zve3kyLwLvYEnBCSOrUgJ3xQqzhRNovGcy', 'operator@smartbee.com', 'a4a14179-881c-48db-9551-b490ba968e93');

INSERT INTO CRM_PERMISSION(id, role_id, authority) VALUES ('bf815dfe-bd85-4a2a-9128-22d0851f476a',
                                                           'a4a14179-881c-48db-9551-b490ba968e91', 'CREATE_COMPANY');
INSERT INTO CRM_PERMISSION(id, role_id, authority) VALUES ('bf815dfe-bd85-4a2a-9128-22d0851f476b',
                                                           'a4a14179-881c-48db-9551-b490ba968e91', 'MODIFY_COMPANY');
INSERT INTO CRM_PERMISSION(id, role_id, authority) VALUES ('bf815dfe-bd85-4a2a-9128-22d0851f476c',
                                                           'a4a14179-881c-48db-9551-b490ba968e91', 'DELETE_COMPANY');
INSERT INTO CRM_PERMISSION(id, role_id, authority) VALUES ('bf815dfe-bd85-4a2a-9128-22d0851f476d',
                                                           'a4a14179-881c-48db-9551-b490ba968e91', 'VIEW_COMPANY');
INSERT INTO CRM_PERMISSION(id, role_id, authority) VALUES ('bf815dfe-bd85-4a2a-9128-22d0851f476f',
                                                           'a4a14179-881c-48db-9551-b490ba968e91', 'CREATE_CLIENT');
INSERT INTO CRM_PERMISSION(id, role_id, authority) VALUES ('bf815dfe-bd85-4a2a-9128-22d0851f477a',
                                                           'a4a14179-881c-48db-9551-b490ba968e91', 'MODIFY_CLIENT');
INSERT INTO CRM_PERMISSION(id, role_id, authority) VALUES ('bf815dfe-bd85-4a2a-9128-22d0851f477b',
                                                           'a4a14179-881c-48db-9551-b490ba968e91', 'DELETE_CLIENT');
INSERT INTO CRM_PERMISSION(id, role_id, authority) VALUES ('bf815dfe-bd85-4a2a-9128-22d0851f477c',
                                                           'a4a14179-881c-48db-9551-b490ba968e91', 'VIEW_CLIENT');

INSERT INTO CRM_PERMISSION(id, role_id, authority) VALUES ('634275a7-3944-4535-856e-083e17c26607',
                                                           'a4a14179-881c-48db-9551-b490ba968e92', 'MODIFY_COMPANY');
INSERT INTO CRM_PERMISSION(id, role_id, authority) VALUES ('634275a7-3944-4535-856e-083e17c26608',
                                                           'a4a14179-881c-48db-9551-b490ba968e92', 'DELETE_COMPANY');
INSERT INTO CRM_PERMISSION(id, role_id, authority) VALUES ('634275a7-3944-4535-856e-083e17c26609',
                                                           'a4a14179-881c-48db-9551-b490ba968e92', 'VIEW_COMPANY');
INSERT INTO CRM_PERMISSION(id, role_id, authority) VALUES ('634275a7-3944-4535-856e-083e17c26600',
                                                           'a4a14179-881c-48db-9551-b490ba968e92', 'MODIFY_CLIENT');
INSERT INTO CRM_PERMISSION(id, role_id, authority) VALUES ('634275a7-3944-4535-856e-083e17c26606',
                                                           'a4a14179-881c-48db-9551-b490ba968e92', 'DELETE_CLIENT');
INSERT INTO CRM_PERMISSION(id, role_id, authority) VALUES ('634275a7-3944-4535-856e-083e17c26605',
                                                           'a4a14179-881c-48db-9551-b490ba968e92', 'VIEW_CLIENT');

INSERT INTO CRM_PERMISSION(id, role_id, authority) VALUES ('3b2838cd-e484-40de-a2d5-a9758bcef03a',
                                                           'a4a14179-881c-48db-9551-b490ba968e93', 'CREATE_COMPANY');
INSERT INTO CRM_PERMISSION(id, role_id, authority) VALUES ('3b2838cd-e484-40de-a2d5-a9758bcef03b',
                                                           'a4a14179-881c-48db-9551-b490ba968e93', 'VIEW_COMPANY');
INSERT INTO CRM_PERMISSION(id, role_id, authority) VALUES ('3b2838cd-e484-40de-a2d5-a9758bcef03c',
                                                           'a4a14179-881c-48db-9551-b490ba968e93', 'CREATE_CLIENT');
INSERT INTO CRM_PERMISSION(id, role_id, authority) VALUES ('3b2838cd-e484-40de-a2d5-a9758bcef03d',
                                                           'a4a14179-881c-48db-9551-b490ba968e93', 'VIEW_CLIENT');
