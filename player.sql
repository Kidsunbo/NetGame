CREATE TABLE player (
    ID PRIMARY KEY, FOREIGN KEY,
    ranking INTEGER, not null, 
    scores INTEGER, not null,
    numbers INTEGER, not null, 
    wins INTEGER, not null, 
); 

--search player
select ID
  from player
 where username=""


-- update ranking
select ID newID
    from player
    order by scores, wins*1.0/numbers, numbers DESC;
--for(int i=0;i=player.length;i++)
update player
   set ranking=i
 where ID=newID --[i]


--victory
update player
   set scores +=
        and numbers++
        and wins++
 where ID = player1 or ID = player2

--loss
update player
   set scores -=
        and numbers++
 where ID = player1 or ID = player2
