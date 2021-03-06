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
    x = W*(float(MARGIN)/DIV)+W*(i/float(DIV))+3+(CX/2);
    y = H*(j/float(DIV))+3+(CY/2);
    
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
    fd.density = 1.0;
    fd.friction = 0.0;
    fd.restitution = 1.0;
    
    
    // Attached the shape to the body using a Fixture
    b.createFixture(fd);
    
    b.setUserData(this);
  }
  
  void killBody() {
    box2d.destroyBody(b);
  }

  // Draw the boundary, if it were at an angle we'd have to do something fancier

}