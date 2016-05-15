class Bar {
  
  float x;
  float y;
  float w;
  float h;
  
  int jfaze=0;
  int jtime = 0;
  Vec2 pos = new Vec2(0.0,0.0);
  
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
    fd.density = 1.0;
    fd.friction = 0.0;
    fd.restitution = 1.0;
    
    // Attached the shape to the body using a Fixture
    b.createFixture(fd);
    
    b.setUserData(this);
  }
  
  
  void display() {
    fill(0);
    stroke(0);
    rectMode(CENTER);
    pos = box2d.getBodyPixelCoord(b);
    rect(pos.x,pos.y,w,h);
  }
  
  void moving(){
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
  
  void jump(){
    
  }
  
  Vec2 getpos(){return pos;}
}
