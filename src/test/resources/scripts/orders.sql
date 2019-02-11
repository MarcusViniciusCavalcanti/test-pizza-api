--------------------- ORDERS ---------------------
INSERT INTO public.orders (id, sizepizza, timeprocess, totalprice, flavors_id) VALUES (1, 'BIGGER', 35, 48, 3);

INSERT INTO public.customizations_orders (customizations_id, orders_id) VALUES (2, 1);
INSERT INTO public.customizations_orders (customizations_id, orders_id) VALUES (1, 1);
INSERT INTO public.customizations_orders (customizations_id, orders_id) VALUES (3, 1);