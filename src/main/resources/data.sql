DELETE
FROM VOTES;
DELETE
FROM FOODS;
DELETE
FROM RESTAURANTS;
DELETE
FROM USER_ROLES;
DELETE
FROM USERS;

ALTER SEQUENCE GLOBAL_SEQ RESTART WITH 100000;

INSERT INTO users (NAME, EMAIL, PASSWORD)
VALUES ('Brad Pitt', 'bradpitt@gmail.com', '{noop}password'),
       ('Christoph Waltz', 'christophwalttz@gmail.com', '{noop}password'),
       ('Michael Fassbender', 'michaelfassbender@gmail.com', '{noop}password'),
       ('Eli Roth', 'admin@gmail.com', '{noop}password'),
       ('Diane Kruger', 'dianekruger@gmail.com', '{noop}password');

INSERT INTO USER_ROLES(ROLE, USER_ID)
VALUES ('USER', 100000),
       ('USER', 100001),
       ('USER', 100002),
       ('ADMIN', 100003),
       ('USER', 100004);

INSERT INTO RESTAURANTS(NAME)
VALUES ('Chleb i Wino restaurant'),
       ('Mandu restaurant'),
       ('Surf Burger restaurant'),
       ('Restauracja Fino restaurant');
INSERT INTO FOODS (PREP_DATE, DESCRIPTION, PRICE, RESTAURANT_ID)
VALUES (now(), 'Tagliolini Thai', 46.90, 100005),
       (now(), 'Pork chop in Old Polish', 49.90, 100005),
       (now(), 'Grilled beef tenderloin steak', 55.90, 100005),
       (now(), 'Tagliolini nero with shrimp and chorizo', 45.90, 100005),
       (now(), 'Roasted ribs in spicy marinade', 55.90, 100005),
       (now(), 'Sweet dumplings with raspberries and white chocolate', 35.00, 100006),
       (now(), 'Traditional dumplings with chopped spinach', 35.50, 100006),
       (now(), 'Dumplings Mandu', 37.00, 100006),
       (now(), 'Tomato soup with handmade egg noodles', 11.50, 100006),
       (now(), 'Dumplings from the stove with spicy chicken', 46.90, 100006),
       (now(), 'Forfiter', 38.90, 100007),
       (now(), 'Caramel with chips and lemonade', 52.90, 100007),
       (now(), 'Boss', 42.90, 100007),
       (now(), 'American with chips and lemonade', 54.90, 100007),
       (now(), 'Double Trouble with chips and lemonade', 52.90, 100007),
       ('2023-07-07', 'Leek risotto with crayfish meat', 48.00, 100008),
       ('2023-07-07', 'Grilled dry watermelon', 78.00, 100008),
       ('2023-07-07', 'Beef tenderloin', 79.00, 100008),
       ('2023-07-07', 'Sturgeon fillet', 95.00, 100008),
       ('2023-07-07', 'Masurian Zander', 86.00, 100008);

INSERT INTO VOTES (VOTE_DATE, USER_ID, RESTAURANT_ID)
VALUES (now(), 100000, 100005),
       (now(), 100001, 100006),
       (now(), 100003, 100007),
       ('2023-07-07', 100000, 100005),
       ('2023-07-07', 100001, 100006),
       ('2023-07-07', 100002, 100007);