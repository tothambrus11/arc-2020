import Turtle.*;

size(600,600);

stroke(100);
for(int i = 50; i < width; i+= 50){
  line(i, 0, i,height);
  line(0, i, width, i);
}
stroke(255,0,0);
Turtle t = new Turtle(this);

t.setX(width/2);
t.setY(400);

t.penDown();
t.right(360*7/12);
t.back(50*4);

int T = 0;
int G = 1;

int[] type = new int[]{T,G,T,G,T,G,T};
double[] q = new double[]
{
  -0.07379180882521665, 
  2.996331164486539,
  -0.2789103106282668,
  3.342005429438114,
  -0.05371435964101718,
  3.605551275463989,
  0.15641647909450063
};

for(int i = 0; i < type.length; i++){
  if(type[i] == T){
    t.left( 360f*(float)q[i]);
  } else if(type[i] == G){
    t.forward((float)q[i]*50f);
  }
}

/*

[COMMAND] TURN -0.07379180882521665 rotations
[COMMAND] GO 2.996331164486539 units
[COMMAND] TURN -0.2789103106282668 rotations
[COMMAND] GO 3.342005429438114 units
[COMMAND] TURN -0.05371435964101718 rotations
[COMMAND] GO 3.605551275463989 units
[COMMAND] TURN 0.15641647909450063 rotations
[COMMAND] POSITION TO CUBE

*/
