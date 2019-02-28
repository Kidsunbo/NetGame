CREATE TABLE game (
    gameID PRIMARY KEY, 
    player1 FOREIGN KEY, 
    player2 FOREIGN KEY, 
    
); 

--START GAME
update game
   set player1 = ""
    and player2 = ""
 where ID in (select ID
                from player
               where username="" or username="")