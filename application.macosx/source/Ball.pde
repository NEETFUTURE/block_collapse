class Ball {
  float r;

  
  Body body;
  Vec2 posi = new Vec2(0.0,0.0);
  color col;
  
  Ball(float x, float y, float r_){
    r = r_;
    makeBody(x, y, r);
    posi.x=x;
    posi.y=y;
    body.setUserData(this);
    body.setLinearVelocity(new Vec2(0,45));
  }
  
  void killBody() {
    box2d.destroyBody(body);
  }
  
  boolean islost(){
    
    if (posi.y > height+r*2 || posi.y < 0 
        || posi.x > W || posi.x < 0) {
      killBody();
      return true;
    }
    return false;
  }
  
  void force(float x_) {
    body.applyForce(new Vec2(0,x_),posi);
  }
  
  Vec2 display() {
    // We look at each body and get its screen position
    posi = box2d.getBodyPixelCoord(body);
    // Get its angle of rotation
    float a = body.getAngle();

    stroke(0,0,0);
    strokeWeight(1);
    fill(0,0,255);
    ellipse(posi.x, posi.y, r*2, r*2);
    return posi;
  }
  
  // Here's our function that adds the particle to the Box2D world
  void makeBody(float x, float y, float r) {
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
    fd.density = 1.0;
    fd.friction = 0.0;
    fd.restitution = 1.0;

    // Attach fixture to body
    body.createFixture(fd);

    body.setAngularVelocity(random(-10, 10));
  }
}
