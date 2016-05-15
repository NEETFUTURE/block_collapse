import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import shiffman.box2d.*; 
import org.jbox2d.common.*; 
import org.jbox2d.dynamics.joints.*; 
import org.jbox2d.collision.shapes.*; 
import org.jbox2d.collision.shapes.Shape; 
import org.jbox2d.common.*; 
import org.jbox2d.dynamics.*; 
import org.jbox2d.dynamics.contacts.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class block_collapse extends PApplet {










boolean space, r, left, right,s;

int W = 800;
int H = 800;
float BARSPEED = W/25;
float Y = H*(1-0.05f);
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


public void setup() {
  
  
  colorMode(RGB,100);
  rectMode(CENTER);
  
  // Initialize box2d physics and create the world
  box2d = new Box2DProcessing(this);
  box2d.createWorld();
  box2d.setGravity(0, -0.5f);

  // Turn on collision listening!
  box2d.listenForCollisions();
  
  //ball = new Ball(W/2.0,H/2.0,20);
  fld = new Field();
  wall1 = new Wall(CX*(MARGIN-0.5f),H/2);
  wall2 = new Wall(CX*(DIV-MARGIN+0.5f),H/2);
  
  bar = new Bar((float)W/2,H*(1-0.05f),(float)W/5.0f,(float)H/50.0f);
  gameset();
  
  // debug
  
  posi = new Vec2((float)W/10.0f,(float)H/50.0f);
}

public void gameset(){
  gamestate = true;
  zanki = 3;
  score = 0;
}

public void draw() {
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

public void mouseClicked() {
    
    fld.click();
} 


public void flip() {
  fill(99);
  rectMode(CORNER);
  rect(0,0,W,H);
  rectMode(CENTER);
}

public void drawlines() {
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

public void dispdata() {
  text("  X: "+mouseX+"  Y: "+mouseY + "  toggle: "+toggle+" ballX: "+ posi.x+" ballY: "+posi.y,10,H-10);
}


  
public int mins(int x,int y){
  return (x<y)?x:y;
}
public int maxs(int x,int y){
  return (x>y)?x:y;
}

public void beginContact(Contact cp) {
  
}


public void keyPressed() {
  //\u4f7f\u7528\u3059\u308b\u30ad\u30fc\u304c\u62bc\u3055\u308c\u305f\u3089\u3001\u5bfe\u5fdc\u3059\u308b\u5909\u6570\u3092true\u306b
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
public void keyReleased() {
  //\u4f7f\u7528\u3059\u308b\u30ad\u30fc\u304c\u96e2\u3055\u308c\u305f\u3089\u3001\u5bfe\u5fdc\u3059\u308b\u5909\u6570\u3092false\u306b
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
public void endContact(Contact cp) {
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
    if(left==true) {p1.force(100.0f);}
    else if(right == true) {p1.force(-100.0f);}
  }else if (o2.getClass() == Ball.class && o1.getClass() == Bar.class) {
    Ball p2 = (Ball) o2;
    if(left==true) {p2.force(100.0f);}
    else if(right == true) {p2.force(-100.0f);}
  }
}
class Ball {
  float r;

  
  Body body;
  Vec2 posi = new Vec2(0.0f,0.0f);
  int col;
  
  Ball(float x, float y, float r_){
    r = r_;
    makeBody(x, y, r);
    posi.x=x;
    posi.y=y;
    body.setUserData(this);
    body.setLinearVelocity(new Vec2(0,45));
  }
  
  public void killBody() {
    box2d.destroyBody(body);
  }
  
  public boolean islost(){
    
    if (posi.y > height+r*2 || posi.y < 0 
        || posi.x > W || posi.x < 0) {
      killBody();
      return true;
    }
    return false;
  }
  
  public void force(float x_) {
    body.applyForce(new Vec2(0,x_),posi);
  }
  
  public Vec2 display() {
    // We look at each body and get its screen position
    posi = box2d.getBodyPixelCoord(body);
    // Get its angle of rotation
    float a = body.getAngle();
    pushMatrix();
    translate(posi.x, posi.y);
    rotate(a);
    
    stroke(0);
    strokeWeight(1);
    fill(0,255,0);
    ellipse(0,0,r*2, r*2);
    // Let's add a line so we can see the rotation
    fill(255,255,0);
    line(0, 0, r, 0);
    popMatrix();
    return posi;
  }
  
  // Here's our function that adds the particle to the Box2D world
  public void makeBody(float x, float y, float r) {
    // Define a body
    BodyDef bd = new BodyDef();
    // Set its position
    bd.position = box2d.coordPixelsToWorld(x, y);
    bd.type = BodyType.DYNAMIC;
    body = box2d.createBody(bd);

    // Make the body's shape a circle
    CircleShape cs = new CircleShape();
    cs.m_radius = box2d.scalarPixelsToWorld(r);

    FixtureDef fd = new FixtureDef();
    fd.shape = cs;
    // Parameters that affect physics
    fd.density = 1.0f;
    fd.friction = 3.0f;
    fd.restitution = 1.0f;

    // Attach fixture to body
    body.createFixture(fd);

    body.setAngularVelocity(random(-10, 10));
  }
}
class Bar {
  
  float x;
  float y;
  float w;
  float h;
  
  int jfaze=0;
  int jtime = 0;
  Vec2 pos = new Vec2(0.0f,0.0f);
  
  // But we also have to make a body for box2d to know about it
  Body b;

  Bar(float x_,float y_, float w_, float h_) {
    x = x_;
    y = y_;
    w = w_;
    h = h_;
    pos.x = x;
    pos.y = y;

    // Define the polygon
    PolygonShape sd = new PolygonShape();
    // Figure out the box2d coordinates
    float box2dW = box2d.scalarPixelsToWorld(w/2);
    float box2dH = box2d.scalarPixelsToWorld(h/2);
    // We're just a box
    sd.setAsBox(box2dW, box2dH);


    // Create the body
    BodyDef bd = new BodyDef();
    bd.type = BodyType.KINEMATIC;
    bd.position.set(box2d.coordPixelsToWorld(x,y));
    b = box2d.createBody(bd);
    
    FixtureDef fd = new FixtureDef();
    fd.shape = sd;
    // Parameters that affect physics
    fd.density = 1.0f;
    fd.friction = 1.0f;
    fd.restitution = 1.0f;
    
    // Attached the shape to the body using a Fixture
    b.createFixture(fd);
    
    b.setUserData(this);
  }
  
  
  public void display() {
    fill(0);
    stroke(0);
    rectMode(CENTER);
    pos = box2d.getBodyPixelCoord(b);
    rect(pos.x,pos.y,w,h);
  }
  
  public void moving(){
    float x=0;
    float y = 0;
    if(left == true && (pos.x > CX*MARGIN)){x = -BARSPEED;}
    else if(right == true && (pos.x < W-CX*MARGIN)){x = BARSPEED;}
    else{x=0;}
    if (space == true && jfaze==0){
      jtime = 2;
      jfaze = 1;
      }
    
    
    if(jfaze==1 && jtime==0){
      jtime = 2;
      jfaze = 2;
    }else if(jfaze==2 && jtime==0){
      jtime = 20;
      jfaze = 3;
    }else if(jfaze==3 && jtime==0){
      jfaze = 0;
    }
    
    if(jtime > 0){jtime--;}
      
    switch (jfaze){
      case 0: y=0;break;
      case 1: y=JUMPSPD;break;
      case 2: y=-JUMPSPD;break;
    }
      
    b.setLinearVelocity(new Vec2(2*x,y));
  
  }
  
  public void jump(){
    
  }
  
  public Vec2 getpos(){return pos;}
}
class Block {
  boolean selected = false;
  int typ = 1;
  
  
  // A boundary is a simple rectangle with x,y,width,and height
  float x;
  float y;
  int i;
  int j;
  float CCX;
  float CCY; 
  
  // But we also have to make a body for box2d to know about it
  Body b;

  Block(int i_,int j_,int typ_) {
    i=i_;
    j=j_;
    x = W*(PApplet.parseFloat(MARGIN)/DIV)+W*(i/PApplet.parseFloat(DIV))+3+(CX/2);
    y = H*(j/PApplet.parseFloat(DIV))+3+(CY/2);
    
    typ = typ_;
    
    
    // Define the polygon
    PolygonShape sd = new PolygonShape();
    // Figure out the box2d coordinates
    float box2dW = box2d.scalarPixelsToWorld(CX/2);
    float box2dH = box2d.scalarPixelsToWorld(CY/2);
    // We're just a box
    sd.setAsBox(box2dW, box2dH);


    // Create the body
    BodyDef bd = new BodyDef();
    bd.type = BodyType.STATIC;
    bd.position.set(box2d.coordPixelsToWorld(x,y));
    b = box2d.createBody(bd);
    
    FixtureDef fd = new FixtureDef();
    fd.shape = sd;
    // Parameters that affect physics
    fd.density = 1.0f;
    fd.friction = 0.0f;
    fd.restitution = 1.0f;
    
    
    // Attached the shape to the body using a Fixture
    b.createFixture(fd);
    
    b.setUserData(this);
  }
  
  public void killBody() {
    box2d.destroyBody(b);
  }

  // Draw the boundary, if it were at an angle we'd have to do something fancier

}
class Field {
  Block[][] blocks = new Block[DIVX][DIVY];

  Field(){
    int i,j;
    for(i=0;i<DIVX;i++){
      for(j=0;j<DIVY;j++){
        if(j==0){
          blocks[i][j] = new Block(
              i,
              j,
              1
          );
        }else if(j>=1 && j<= DIVY-8){
          blocks[i][j] = new Block(
              i,
              j,
              2
          );
        }
        else{
          blocks[i][j] = null;
        }
      }
    }
  }
  
  public void click(){
    if(mouseX > MARGIN*CX && mouseX < W-MARGIN*CX){
      int i =((int)mouseX/((int)CX));
      int j =((int)mouseY/((int)CY));
      blocks[i-MARGIN][j].typ = 0;
    }
  
  }
  
  public void rebuild(){
    int i,j;
    for(i=0;i<DIVX;i++){
      for(j=0;j<DIVY;j++){
        if(j>=1 && j<= DIVY-8 && 
            blocks[i][j]==null){
          blocks[i][j] = new Block(
              i,
              j,
              2
          );
        }
        
      }
    }
  }
  
  public void display() {
    int i,j;
    
    noFill();
    strokeJoin(BEVEL);
    stroke(0,0,0);
    strokeWeight(3);
    rectMode(CORNER);
    
    if (zanki == 3){
      fill(0,100,255);
    }else if (zanki == 2) {
      fill(100,255,0);
    }else{
      fill(255,0,0);
    }
    
    for (i=0;i<DIVX;i++){
      for (j=0;j<DIVY;j++){
        if (blocks[i][j]==null) continue;
        if (blocks[i][j].typ!=0 || blocks[i][j].selected){
          rect(W*(PApplet.parseFloat(MARGIN)/DIV)+W*(i/PApplet.parseFloat(DIV))+3,
               H*(j/PApplet.parseFloat(DIV))+3,
               CX-6,
               CY-6);
        }else if(blocks[i][j].typ == 0 && blocks[i][j]!=null){
          blocks[i][j].killBody();
          blocks[i][j] = null;
        }
      }
    }
    
    rectMode(CENTER);
    strokeJoin(MITER);
  }
}
class Wall {  
  // A boundary is a simple rectangle with x,y,width,and height
  float x;
  float y; 
  
  // But we also have to make a body for box2d to know about it
  Body b;

  Wall(float x_,float y_) {

    x = x_;
    y = y_;
    
    
    // Define the polygon
    PolygonShape sd = new PolygonShape();
    // Figure out the box2d coordinates
    float box2dW = box2d.scalarPixelsToWorld(CX/2);
    float box2dH = box2d.scalarPixelsToWorld(H/2);
    // We're just a box
    sd.setAsBox(box2dW, box2dH);


    // Create the body
    BodyDef bd = new BodyDef();
    bd.type = BodyType.STATIC;
    bd.position.set(box2d.coordPixelsToWorld(x,y));
    b = box2d.createBody(bd);
    
    
    FixtureDef fd = new FixtureDef();
    fd.shape = sd;
    // Parameters that affect physics
    fd.density = 1.0f;
    fd.friction = 0.0f;
    fd.restitution = 1.0f;
    
    // Attached the shape to the body using a Fixture
    b.createFixture(fd);
    
    b.setUserData(this);
  }
  
  public void killBody() {
    box2d.destroyBody(b);
  }
  
  public void display() {
    rectMode(CENTER);

    noStroke();
    fill(0);
    rect(x,y,CX,H);
  }

  // Draw the boundary, if it were at an angle we'd have to do something fancier

}
  public void settings() {  size(801,801);  smooth(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "block_collapse" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
