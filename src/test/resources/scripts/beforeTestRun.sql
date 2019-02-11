--------------------- ADDONS ---------------------
INSERT INTO public.addons_time_process (id, name, time) VALUES (1, 'add five minute in process', 5);
INSERT INTO public.addons_price (id, name, price) VALUES (2, 'add five in price', 5);
INSERT INTO public.addons_price (id, name, price) VALUES (3, 'add three in price', 3);

--------------------- FLAVORS ---------------------
INSERT INTO public.flavors (id, createdat, name, updatedat, addon_id) VALUES (1, '2019-02-09 16:25:25.088154', 'Calabresa', '2019-02-09 16:25:25.088199', null);
INSERT INTO public.flavors (id, createdat, name, updatedat, addon_id) VALUES (2, '2019-02-09 16:25:25.097714', 'Marguerita', '2019-02-09 16:25:25.097738', null);
INSERT INTO public.flavors (id, createdat, name, updatedat, addon_id) VALUES (3, '2019-02-09 16:25:25.100764', 'Portuguesa', '2019-02-09 16:25:25.100781', 1);

--------------------- CUSTOMIZATIONS ---------------------
INSERT INTO public.customizations (id, createdat, name, updatedat) VALUES (1, '2019-02-10 21:27:29.308618', 'Extra Bacon', '2019-02-10 21:27:29.308649');
INSERT INTO public.customizations (id, createdat, name, updatedat) VALUES (2, '2019-02-10 21:27:29.324942', 'Sem cebola', '2019-02-10 21:27:29.324959');
INSERT INTO public.customizations (id, createdat, name, updatedat) VALUES (3, '2019-02-10 21:27:29.329992', 'Bordar Recheada', '2019-02-10 21:27:29.330008');

INSERT INTO public.customizations_addons (addons_id, customizations_id) VALUES (1, 3);
INSERT INTO public.customizations_addons (addons_id, customizations_id) VALUES (3, 2);
INSERT INTO public.customizations_addons (addons_id, customizations_id) VALUES (3, 1);
