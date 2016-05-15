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
        }else{
          blocks[i][j] = null;
        }
      }
    }
  }
  
  void click(){
    if(mouseX > MARGIN*CX && mouseX < W-MARGIN*CX){
      int i =((int)mouseX/((int)CX));
      int j =((int)mouseY/((int)CY));
      blocks[i-MARGIN][j].typ = 0;
    }
  
  }
  
  void display() {
    int i,j;
    
    noFill();
    strokeJoin(BEVEL);
    stroke(0,0,0);
    strokeWeight(3);
    rectMode(CORNER);
    
    for (i=0;i<DIVX;i++){
      for (j=0;j<DIVY;j++){
        if (blocks[i][j]==null) continue;
        if (blocks[i][j].typ!=0 || blocks[i][j].selected){
          rect(W*(float(MARGIN)/DIV)+W*(i/float(DIV))+3,
               H*(j/float(DIV))+3,
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
