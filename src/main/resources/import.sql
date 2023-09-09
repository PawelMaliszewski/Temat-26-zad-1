INSERT INTO account (FIRST_NAME, LAST_NAME, EMAIL, BALANCE)
VALUES
    ('Jaś', 'Fasola', 'jas@fasola.pl', '250'),
    ('Kuba', 'Rozpruwacz', 'kuba@rozpruwacz.pl', '2500');


INSERT INTO GAME(NAME_OF_TEAMA, NAME_OF_TEAMB , TEAMAWIN_RATE, TEAMBWIN_RATE, DRAW_RATE, RESULT)
VALUES
    ('Drużyna A', 'Drużyna B', '1.14', '3.5', '2', '3'),
    ('Drużyna C', 'Drużyna D', '2.14', '2.5', '1.5', '3'),
    ('Drużyna Y', 'Drużyna R', '1.28', '5.5', '2.2', '3'),
    ('Drużyna T', 'Drużyna D', '2.14', '1.5', '1.8', '3'),
    ('Drużyna P', 'Drużyna T', '3.75', '2.8', '2.15', '3'),
    ('Drużyna O', 'Drużyna H', '3.65', '3.35', '2.1', '3'),
    ('Drużyna G', 'Drużyna Q', '5.45', '3.5', '1.7', '3'),
    ('Drużyna Z', 'Drużyna N', '1.22', '6.5', '13', '3'),
    ('Drużyna X', 'Drużyna L', '1.45', '4.5', '7.25', '3');

INSERT INTO BET(ACCOUNT_ID, BET_ID, BET_MONEY)
VALUES
    (1, 1, '25'),
    (1, 2, '50'),
    (1, 3, '100'),
    (2, 4, '35'),
    (2, 5, '1000'),
    (2, 6, '200');

INSERT INTO BET_GAME (GAME_ID , RESULT)
VALUES
    (1, 0),
    (2, 1),
    (6, 2),
    (1, 2),
    (5, 3),
    (4, 0),
    (8, 2),
    (5, 1),
    (6, 1),
    (6, 2),
    (9, 0);

INSERT INTO BET_BET_GAMES(BETS_BET_ID , BET_GAMES_ID)
VALUES
    (1, 1),
    (2, 2),
    (3, 3),
    (4, 4),
    (5, 5),
    (6, 6);












