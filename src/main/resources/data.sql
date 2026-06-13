-- Inserción de Artistas (IDs a partir del 1000 para no chocar con las secuencias de Hibernate)
INSERT INTO ARTISTA (id, nombre, version) VALUES (1001, 'Daft Punk', 0);
INSERT INTO ARTISTA (id, nombre, version) VALUES (1002, 'The Weeknd', 0);
INSERT INTO ARTISTA (id, nombre, version) VALUES (1003, 'Rosalía', 0);
INSERT INTO ARTISTA (id, nombre, version) VALUES (1004, 'Melendi', 0);
INSERT INTO ARTISTA (id, nombre, version) VALUES (1005, 'Maná', 0);
INSERT INTO ARTISTA (id, nombre, version) VALUES (1006, 'Mägo de Oz', 0);
INSERT INTO ARTISTA (id, nombre, version) VALUES (1007, 'Queen', 0);

-- Inserción de Estilos Musicales
INSERT INTO ESTILO_MUSICAL (id, estilo) VALUES (1001, 'Electrónica');
INSERT INTO ESTILO_MUSICAL (id, estilo) VALUES (1002, 'Pop / R&B');
INSERT INTO ESTILO_MUSICAL (id, estilo) VALUES (1003, 'Flamenco Urbano');
INSERT INTO ESTILO_MUSICAL (id, estilo) VALUES (1004, 'Pop Rock');
INSERT INTO ESTILO_MUSICAL (id, estilo) VALUES (1005, 'Rock Latino');
INSERT INTO ESTILO_MUSICAL (id, estilo) VALUES (1006, 'Folk Metal');
INSERT INTO ESTILO_MUSICAL (id, estilo) VALUES (1007, 'Classic Rock');

-- Inserción de Álbumes
INSERT INTO ALBUM (id, nombre, imagen, id_artista, version) VALUES (1001, 'Discovery', 'https://upload.wikimedia.org/wikipedia/en/a/ae/Daft_Punk_-_Discovery.jpg', 1001, 0);
INSERT INTO ALBUM (id, nombre, imagen, id_artista, version) VALUES (1002, 'After Hours', 'https://upload.wikimedia.org/wikipedia/en/c/c1/The_Weeknd_-_After_Hours.png', 1002, 0);
INSERT INTO ALBUM (id, nombre, imagen, id_artista, version) VALUES (1003, 'Motomami', 'https://upload.wikimedia.org/wikipedia/en/4/4b/Rosal%C3%ADa_-_Motomami.png', 1003, 0);
INSERT INTO ALBUM (id, nombre, imagen, id_artista, version) VALUES (1004, 'Quítate las gafas', 'https://upload.wikimedia.org/wikipedia/en/b/b2/Quitate_las_Gafas.jpg', 1004, 0);
INSERT INTO ALBUM (id, nombre, imagen, id_artista, version) VALUES (1005, 'Grandes Éxitos', 'https://upload.wikimedia.org/wikipedia/en/2/2c/Man%C3%A1_Grandes_Exitos.jpg', 1005, 0);
INSERT INTO ALBUM (id, nombre, imagen, id_artista, version) VALUES (1006, 'La leyenda de la Mancha', 'https://upload.wikimedia.org/wikipedia/en/1/1a/La_Leyenda_de_la_Mancha.jpg', 1006, 0);
INSERT INTO ALBUM (id, nombre, imagen, id_artista, version) VALUES (1007, 'Finisterra', 'https://upload.wikimedia.org/wikipedia/en/9/9f/Finisterra.jpg', 1006, 0);
INSERT INTO ALBUM (id, nombre, imagen, id_artista, version) VALUES (1008, 'Gaia', 'https://upload.wikimedia.org/wikipedia/en/d/da/Gaia_%28M%C3%A4go_de_Oz_album%29.jpg', 1006, 0);
INSERT INTO ALBUM (id, nombre, imagen, id_artista, version) VALUES (1009, 'Greatest Hits I', 'https://upload.wikimedia.org/wikipedia/en/1/18/Queen_Greatest_Hits.png', 1007, 0);
INSERT INTO ALBUM (id, nombre, imagen, id_artista, version) VALUES (1010, 'Greatest Hits II', 'https://upload.wikimedia.org/wikipedia/en/5/53/Queen_Greatest_Hits_II.png', 1007, 0);

-- Inserción de Canciones Originales
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1001, 'One More Time', 320, 1500000, 1001, 1001, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1002, 'Harder, Better, Faster, Stronger', 224, 2300000, 1001, 1001, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1003, 'Blinding Lights', 200, 5000000, 1002, 1002, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1004, 'Save Your Tears', 215, 3200000, 1002, 1002, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1005, 'SAOKO', 137, 1800000, 1003, 1003, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1006, 'DESPECHÁ', 156, 4100000, 1003, 1003, CURRENT_TIMESTAMP, 0);

-- Inserción de Canciones: Melendi - Quítate las gafas (Album 1004, Estilo 1004)
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1007, 'Flores de agua y plomo', 234, 100500, 1004, 1004, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1008, 'Destino o casualidad', 255, 320000, 1004, 1004, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1009, 'Hijos del mal', 221, 95000, 1004, 1004, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1010, 'Desde que estamos juntos', 201, 150000, 1004, 1004, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1011, 'La casa no es igual', 212, 110000, 1004, 1004, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1012, 'Mi mayor fortuna', 198, 88000, 1004, 1004, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1013, 'Un amor tan grande', 244, 99000, 1004, 1004, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1014, 'Soy tu superhéroe', 222, 102000, 1004, 1004, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1015, 'Existen los milagros', 205, 87000, 1004, 1004, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1016, 'Quítate las gafas', 211, 210000, 1004, 1004, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1017, 'Yo me veo contigo', 199, 134000, 1004, 1004, CURRENT_TIMESTAMP, 0);

-- Inserción de Canciones: Maná - Grandes Éxitos (Album 1005, Estilo 1005)
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1018, 'Rayando el sol', 256, 500000, 1005, 1005, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1019, 'Oye mi amor', 270, 480000, 1005, 1005, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1020, 'En el muelle de San Blas', 357, 520000, 1005, 1005, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1021, 'Vivir sin aire', 290, 410000, 1005, 1005, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1022, 'Clavado en un bar', 312, 390000, 1005, 1005, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1023, 'Mariposa traicionera', 265, 430000, 1005, 1005, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1024, 'Labios compartidos', 278, 450000, 1005, 1005, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1025, 'Corazón espinado', 276, 550000, 1005, 1005, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1026, 'Bendita tu luz', 260, 400000, 1005, 1005, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1027, 'Mi verdad', 273, 310000, 1005, 1005, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1028, 'El verdadero amor perdona', 234, 290000, 1005, 1005, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1029, 'Te lloré un río', 294, 305000, 1005, 1005, CURRENT_TIMESTAMP, 0);

-- Inserción de Canciones: Mägo de Oz - La leyenda de la Mancha (Album 1006, Estilo 1006)
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1030, 'En un lugar...', 124, 150000, 1006, 1006, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1031, 'El santo grial', 311, 280000, 1006, 1006, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1032, 'La leyenda de la Mancha', 255, 310000, 1006, 1006, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1033, 'Noche toledana', 98, 90000, 1006, 1006, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1034, 'Molinos de viento', 253, 900000, 1006, 1006, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1035, 'Dime con quien andas', 332, 110000, 1006, 1006, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1036, 'Maritormes', 260, 105000, 1006, 1006, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1037, 'El pacto', 342, 120000, 1006, 1006, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1038, 'La ínsula de Barataria', 178, 95000, 1006, 1006, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1039, 'El templo del adiós', 290, 250000, 1006, 1006, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1040, 'Réquiem', 430, 180000, 1006, 1006, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1041, 'Ancha es Castilla', 225, 200000, 1006, 1006, CURRENT_TIMESTAMP, 0);

-- Inserción de Canciones: Mägo de Oz - Finisterra (Album 1007, Estilo 1006)
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1042, 'Prólogo', 120, 110000, 1007, 1006, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1043, 'Satania', 495, 320000, 1007, 1006, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1044, 'La cruz de Santiago', 315, 290000, 1007, 1006, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1045, 'La danza del fuego', 314, 450000, 1007, 1006, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1046, 'Hasta que el cuerpo aguante', 272, 510000, 1007, 1006, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1047, 'El señor de los gramillos', 300, 190000, 1007, 1006, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1048, 'Polla dura no cree en Dios', 270, 180000, 1007, 1006, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1049, 'Maite Zaitut', 200, 220000, 1007, 1006, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1050, 'Duerme...', 262, 140000, 1007, 1006, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1051, 'Es hora de marchar', 303, 160000, 1007, 1006, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1052, 'Fiesta pagana', 296, 950000, 1007, 1006, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1053, 'El que quiera entender que entienda', 368, 280000, 1007, 1006, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1054, 'Los renglones torcidos de Dios', 392, 190000, 1007, 1006, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1055, 'Kelpie', 290, 150000, 1007, 1006, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1056, 'Tres tristes tigres', 163, 110000, 1007, 1006, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1057, 'A costa da morte', 214, 130000, 1007, 1006, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1058, 'La santa compaña', 334, 180000, 1007, 1006, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1059, 'Concierto para ellos', 283, 210000, 1007, 1006, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1060, 'Finisterra', 912, 140000, 1007, 1006, CURRENT_TIMESTAMP, 0);

-- Inserción de Canciones: Mägo de Oz - Gaia (Album 1008, Estilo 1006)
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1061, 'Overture MDXX', 235, 150000, 1008, 1006, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1062, 'Gaia', 666, 310000, 1008, 1006, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1063, 'La conquista', 293, 210000, 1008, 1006, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1064, 'Alma', 400, 250000, 1008, 1006, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1065, 'La costa del silencio', 270, 750000, 1008, 1006, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1066, 'El árbol de la noche triste', 290, 190000, 1008, 1006, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1067, 'La rosa de los vientos', 255, 380000, 1008, 1006, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1068, 'La leyenda de la llorona', 260, 210000, 1008, 1006, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1069, 'Van a rodar cabezas', 390, 180000, 1008, 1006, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1070, 'El atrapasueños', 258, 220000, 1008, 1006, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1071, 'Si te vas', 360, 240000, 1008, 1006, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1072, 'La venganza de Gaia', 665, 300000, 1008, 1006, CURRENT_TIMESTAMP, 0);

-- Inserción de Canciones: Queen - Greatest Hits I (Album 1009, Estilo 1007)
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1073, 'Bohemian Rhapsody', 354, 9500000, 1009, 1007, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1074, 'Another One Bites the Dust', 216, 8200000, 1009, 1007, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1075, 'Killer Queen', 180, 5100000, 1009, 1007, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1076, 'Fat Bottomed Girls', 204, 3800000, 1009, 1007, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1077, 'Bicycle Race', 181, 2900000, 1009, 1007, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1078, 'You''re My Best Friend', 172, 4200000, 1009, 1007, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1079, 'Don''t Stop Me Now', 209, 7800000, 1009, 1007, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1080, 'Save Me', 228, 2500000, 1009, 1007, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1081, 'Crazy Little Thing Called Love', 162, 4500000, 1009, 1007, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1082, 'Somebody to Love', 296, 6100000, 1009, 1007, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1083, 'Now I''m Here', 250, 1800000, 1009, 1007, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1084, 'Good Old-Fashioned Lover Boy', 174, 2100000, 1009, 1007, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1085, 'Play the Game', 210, 1950000, 1009, 1007, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1086, 'Flash', 168, 1500000, 1009, 1007, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1087, 'Seven Seas of Rhye', 167, 1750000, 1009, 1007, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1088, 'We Will Rock You', 121, 9100000, 1009, 1007, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1089, 'We Are the Champions', 179, 8800000, 1009, 1007, CURRENT_TIMESTAMP, 0);

-- Inserción de Canciones: Queen - Greatest Hits II (Album 1010, Estilo 1007)
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1090, 'A Kind of Magic', 262, 3200000, 1010, 1007, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1091, 'Under Pressure', 236, 6500000, 1010, 1007, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1092, 'Radio Ga Ga', 343, 4100000, 1010, 1007, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1093, 'I Want It All', 241, 3800000, 1010, 1007, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1094, 'I Want to Break Free', 258, 6200000, 1010, 1007, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1095, 'Innuendo', 387, 2100000, 1010, 1007, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1096, 'It''s a Hard Life', 248, 1750000, 1010, 1007, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1097, 'Breakthru', 247, 1850000, 1010, 1007, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1098, 'Who Wants to Live Forever', 315, 3400000, 1010, 1007, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1099, 'Headlong', 278, 1500000, 1010, 1007, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1100, 'The Miracle', 302, 1950000, 1010, 1007, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1101, 'I''m Going Slightly Mad', 262, 1700000, 1010, 1007, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1102, 'The Invisible Man', 238, 1600000, 1010, 1007, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1103, 'Hammer to Fall', 220, 2100000, 1010, 1007, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1104, 'Friends Will Be Friends', 247, 1900000, 1010, 1007, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1105, 'The Show Must Go On', 271, 5800000, 1010, 1007, CURRENT_TIMESTAMP, 0);
INSERT INTO CANCION (id, titulo, duracion, cant_repro, id_album, id_estilo, fecha_creacion, version) VALUES (1106, 'One Vision', 242, 2200000, 1010, 1007, CURRENT_TIMESTAMP, 0);

-- Inserción de Usuarios por defecto
INSERT INTO USUARIO (username, nombre, apellidos, password, email, rol, version) VALUES ('user', 'Jose', 'Gomez', '$2a$10$wRPTN6.fK3y1QzK07t1.PexKk289v4s0.t3w.gP/wY3vLq2w1z3rK', 'user@kreitefy.com', 'USUARIO', 0);
INSERT INTO USUARIO (username, nombre, apellidos, password, email, rol, version) VALUES ('admin', 'Maria', 'Lopez', '$2a$10$wRPTN6.fK3y1QzK07t1.PexKk289v4s0.t3w.gP/wY3vLq2w1z3rK', 'admin@kreitefy.com', 'ADMIN', 0);
