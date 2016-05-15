import shiffman.box2d.*;
import org.jbox2d.common.*;
import org.jbox2d.dynamics.joints.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.*;

boolean space, r, left, right,s;

int W = 800;
int H = 800;
float BARSPEED = W/25;
float Y = H*(1-0.05);
int DIV = 16;
int MARGIN = 3;
int DIVX = DIV -2*MARGIN;
int DIVY = DIV;
float CX = W/DIV;
float CY = H/DIV;
float JUMPSPD = 30;
int score = 0;

Vec2 posi;
boolean gamestate;
int zanki;

boolean toggle = false;
int x = W/2;
int a_i,a_j,b_i,b_j;
Field fld;
Bar bar;
Wall wall1,wall2;
Ball ball;
Box2DProcessing box2d;


void setup() {
  size(W+1,H+1);
  smooth();
  colorMode(RGB,100);
  rectMode(CENTER);
  
  // Initialize box2d physics and create the world
  box2d = new Box2DProcessing(this);
  box2d.createWorld();
  box2d.setGravity(0, -0.5);

  // Turn on collision listening!
  box2d.listenForCollisions();
  
  //ball = new Ball(W/2.0,H/2.0,20);
  fld = new Field();
  wall1 = new Wall(CX*(MARGIN-0.5),H/2);
  wall2 = new Wall(CX*(DIV-MARGIN+0.5),H/2);
  
  bar = new Bar((float)W/2,H*(1-0.05),(float)W/5.0,(float)H/50.0);
  gameset();
  
  // debug
  
  posi = new Vec2((float)W/10.0,(float)H/50.0);
}

void gameset(){
  gamestate = true;
  zanki = 3;
  score = 0;
}

void draw() {
  flip();
  box2d.step();
  
  if(mousePressed){
    if(toggle==false){
      a_i = (int)(mouseX/CX)-MARGIN;
      a_j = (int)(mouseY/CY);
      toggle = true;    
    }
  }else{
    if(toggle==true){
      b_i = (int)(mouseX/CX)-MARGIN;
      b_j = (int)(mouseY/CY);
      for(int i=mins(a_i,b_i);i<maxs(a_i,b_i)+1;i++){
        for(int j=mins(a_j,b_j);j<maxs(a_j,b_j)+1;j++){
          fld.blocks[i][j]=new Block(
              i,
              j,
              2
          );
        }
      }
      toggle = false;
    }
  }
  
  if(keyPressed){
     if(r == true && ball==null && gamestate == true){
       Vec2 a = bar.getpos();
       ball = new Ball(a.x,a.y-CY,13);
     }
     if(s==true && gamestate == false){
       zanki = 3;
       gamestate = true;
       gameset();
       fld.rebuild();
     }
  }
  if(ball!=null && ball.islost() == true){
    ball = null;
    zanki--;
  }
  
  
  fill(0);
  
  //drawlines();
  fld.display();
  
  if(ball!=null)posi = ball.display();
  
  fill(0);
  bar.moving();
  bar.display();
  wall1.display();
  wall2.display();
  //rect(x,H*(1-0.05),W/10,H/50);
  
  if(zanki==0){
    gamestate = false;
  }
  if(gamestate == false){
       textSize(90);
       textAlign(CENTER);
       text("GAME OVER",W/2,H/4);
       textSize(70);
       text("PUSH 'S' KEY",W/2,2*H/3-50);
       text("TO RESTART",W/2,2*H/3);
     }
  textAlign(RIGHT);
  textSize(20);
  text("LIFE: " + zanki,CX+20,H/2);
  text("SCORE: " + score,CX+40,H/2+25);
  //textAlign(LEFT);
  //textSize(15);
  //dispdata();

}

void mouseClicked() {
    
    fld.click();
} 


void flip() {
  fill(99);
  rectMode(CORNER);
  rect(0,0,W,H);
  rectMode(CENTER);
}

void drawlines() {
  float h = W/DIVY;
  float W0 = W/2; float H0 = H/2;
  float i;
  stroke(0,0,0);
  strokeWeight(1);
  for(i=0;i < (W/2);i+=h){
    line(W0+i,0,W0+i,H);
    line(W0-i,0,W0-i,H);
    line(0,H0+i,W,H0+i);
    line(0,H0-i,W,H0-i);
  }
}

void dispdata() {
  text("  X: "+mouseX+"  Y: "+mouseY + "  toggle: "+toggle+" ballX: "+ posi.x+" ballY: "+posi.y,10,H-10);
}


  
int mins(int x,int y){
  return (x<y)?x:y;
}
int maxs(int x,int y){
  return (x>y)?x:y;
}

void beginContact(Contact cp) {
  
}


void keyPressed() {
  //使用するキーが押されたら、対応する変数をtrueに
  switch(key) {
    case ' ':
      space = true;
      break;
    case 'r':
      r = true;
      break;
    case 's':
      s = true;
      break;
  }
  switch(keyCode) {
    case LEFT:
      left = true;
      break;
    case RIGHT:
      right = true;
      break;
  }
}
void keyReleased() {
  //使用するキーが離されたら、対応する変数をfalseに
  switch(key) {
    case ' ':
      space = false;
      break;
    case 'r':
      r = false;
      break;
    case 's':
      s = false;
      break;
  }
  switch(keyCode) {
    case LEFT:
      left = false;
      break;
    case RIGHT:
      right = false;
      break;
  }
}

// Objects stop touching each other
void endContact(Contact cp) {
  // Get both fixtures
  Fixture f1 = cp.getFixtureA();
  Fixture f2 = cp.getFixtureB();
  // Get both bodies
  Body b1 = f1.getBody();
  Body b2 = f2.getBody();

  // Get our objects that reference these bodies
  Object o1 = b1.getUserData();
  Object o2 = b2.getUserData();

  if (o1.getClass() == Ball.class && o2.getClass() == Block.class) {
    Block p2 = (Block) o2;
    score++;
    if(p2.typ == 2){
      //p2.killBody();
      fld.blocks[p2.i][p2.j].typ=0;
    }
  }else if (o2.getClass() == Ball.class && o1.getClass() == Block.class) {
    Block p1 = (Block) o1;
    score++;
    if(p1.typ == 2){
      //p1.killBody();
      fld.blocks[p1.i][p1.j].typ=0;
    }
  }
  else if (o1.getClass() == Ball.class && o2.getClass() == Bar.class) {
    Ball p1 = (Ball) o1;
    if(left==true) {p1.force(100.0);}
    else if(right == true) {p1.force(-100.0);}
  }else if (o2.getClass() == Ball.class && o1.getClass() == Bar.class) {
    Ball p2 = (Ball) o2;
    if(left==true) {p2.force(100.0);}
    else if(right == true) {p2.force(-100.0);}
  }
}


