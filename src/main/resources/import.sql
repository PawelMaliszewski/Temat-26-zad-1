


INSERT INTO GAME(NAME_OF_TEAMA, NAME_OF_TEAMB , TEAMAWIN_RATE, TEAMBWIN_RATE, DRAW_RATE, GAME_RESULT)
VALUES
    ('Drużyna A', 'Drużyna B', '1.14', '3.5', '2', 'WAITING'),
    ('Drużyna C', 'Drużyna D', '2.14', '2.5', '1.5', 'WAITING'),
    ('Drużyna Y', 'Drużyna R', '1.28', '5.5', '2.2', 'WAITING'),
    ('Drużyna T', 'Drużyna D', '2.14', '1.5', '1.8', 'WAITING'),
    ('Drużyna P', 'Drużyna T', '3.75', '2.8', '2.15', 'WAITING'),
    ('Drużyna O', 'Drużyna H', '3.65', '3.35', '2.1', 'WAITING'),
    ('Drużyna G', 'Drużyna Q', '5.45', '3.5', '1.7', 'WAITING'),
    ('Drużyna Z', 'Drużyna N', '1.22', '6.5', '13', 'WAITING'),
    ('Drużyna X', 'Drużyna L', '1.45', '4.5', '7.25', 'WAITING');

INSERT INTO BET(BET_MONEY, BET_ID)
VALUES
    ('100', '1'),
    ('55.50', '2'),
    ('60', '3'),
    ('5', '4'),
    ('12', '5'),
    ('100000', '6');


INSERT INTO BET_GAME (BET_ID, GAME_ID, GAME_RESULT,  GAME_TITLE, WIN_RATE)
VALUES
    ('1' ,'1' , 'TEAM_A_WON', 'Drużyna A VS Drużyna B', '1.14'),
    ('1', '2', 'TEAM_A_WON', 'Drużyna C VS Drużyna D', '2.14'),
    ('2', '3', 'TEAM_A_WON', 'Drużyna Y VS Drużyna R', '1.28'),
    ('2', '4','TEAM_B_WON', 'Drużyna T VS Drużyna D', '1.8'),
    ('3', '5','TEAM_B_WON', 'Drużyna P VS Drużyna T', '2.15'),
    ('4', '6','TEAM_B_WON', 'Drużyna O VS Drużyna H', '2.1'),
    ('4', '7','TEAM_A_WON', 'Drużyna G VS Drużyna Q', '5.45'),
    ('5', '8','DRAW', 'Drużyna Z VS Drużyna N', '6.5'),
    ('6', '9','DRAW', 'Drużyna X VS Drużyna L', '4.5'),
    ('6', '4', 'TEAM_B_WON', 'Drużyna T VS Drużyna D', '1.8'),
    ('6', '7', 'DRAW', 'Drużyna G VS Drużyna Q', '3.5');
















