package myGame.Frames;

import javafx.scene.effect.Light;

import java.util.LinkedList;

public interface INetwork {
     public String getsnakeAName(); // 从网络读取蛇A的名字
     public String getsnakeBName(); // 从网络读取蛇B的用户名push

     public void sendSnakebodyA(); //(用 snakebody.get), 把蛇的更新后的Linkedlist<Point> 身子发出去，
     public void sendSnakeHeadA(); // 把蛇头的新的坐标发出去 Point, snake.get当前的x,y值，并生成新的Point, 发出去

     public  void sendSnakebodyB(); //把蛇的更新后的Linkedlist<Point> 身子发出去，
     public void sendSnakeHeadB(); // 把蛇头的新的坐标发出去 Point

     public  Light.Point reHeadofSnakeA(); // 收回蛇的头信息；先收到Point 再用setx(),sety() 方法为蛇设置x,y  (x,y)
     public LinkedList<Light.Point> reBodyOfSnakeA(); //收回蛇的身子的信息（x，y）

     public Light.Point reHeadofSnakeB(); // 收回蛇的头信息；(x,y)
     public  LinkedList<Light.Point> reBodyOfSnakeB(); // 收回蛇的身子的信息（x，y）

     public void sendDirectionA();  //把方向发出去
     public void sendFirectionB();

     public Constants.DIRECTIONS reDirectionA();   //收新的方向
     public Constants.DIRECTIONS reDirectionB();

     public void sendScore (); // 把胜利者发出去，数据库胜场加一；

     public int  getPosition(); // decide SnakeA and B
}
