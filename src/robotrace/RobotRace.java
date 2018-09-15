package robotrace;

import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.gl2.GLUT;
import java.awt.Color;
import java.awt.Font;
import static java.lang.Math.*;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import static javafx.util.Duration.seconds;
import javax.media.opengl.GL;
import static javax.media.opengl.GL.GL_BLEND;
import static javax.media.opengl.GL.GL_COLOR_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_TEST;
import static javax.media.opengl.GL.GL_FRONT_AND_BACK;
import static javax.media.opengl.GL.GL_LESS;
import static javax.media.opengl.GL.GL_NICEST;
import static javax.media.opengl.GL.GL_ONE_MINUS_SRC_ALPHA;
import static javax.media.opengl.GL.GL_SRC_ALPHA;
import static javax.media.opengl.GL.GL_TEXTURE_2D;
import javax.media.opengl.GL2;
import static javax.media.opengl.GL2.*;
import static javax.media.opengl.GL2ES1.GL_LIGHT_MODEL_AMBIENT;
import static javax.media.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT;
import static javax.media.opengl.GL2GL3.GL_FILL;
import static javax.media.opengl.GL2GL3.GL_QUADS;
import static javax.media.opengl.GLProfile.GL2;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_AMBIENT;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_AMBIENT_AND_DIFFUSE;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_COLOR_MATERIAL;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_DIFFUSE;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_LIGHT0;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_LIGHT1;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_LIGHTING;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_NORMALIZE;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_POSITION;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_SHININESS;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_SPECULAR;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;
import javax.media.opengl.glu.GLU;
import static robotrace.Base.head;


/**
 * Handles all of the RobotRace graphics functionality,
 * which should be extended per the assignment.
 * 
 * OpenGL functionality:
 * - Basic commands are called via the gl object;
 * - Utility commands are called via the glu and
 *   glut objects;
 * 
 * GlobalState:
 * The gs object contains the GlobalState as described
 * in the assignment:
 * - The camera viewpoint angles, phi and theta, are
 *   changed interactively by holding the left mouse
 *   button and dragging;
 * - The camera view width, vWidth, is changed
 *   interactively by holding the right mouse button
 *   and dragging upwards or downwards;
 * - The center point can be moved up and down by
 *   pressing the 'q' and 'z' keys, forwards and
 *   backwards with the 'w' and 's' keys, and
 *   left and right with the 'a' and 'd' keys;
 * - Other settings are changed via the menus
 *   at the top of the screen.
 * 
 * Textures:
 * Place your "track.jpg", "brick.jpg", "head.jpg",
 * and "torso.jpg" files in the same folder as this
 * file. These will then be loaded as the texture
 * objects track, bricks, head, and torso respectively.
 * Be aware, these objects are already defined and
 * cannot be used for other purposes. The texture
 * objects can be used as follows:
 * 
 * gl.glColor3f(1f, 1f, 1f);
 * track.bind(gl);
 * gl.glBegin(GL_QUADS);
 * gl.glTexCoord2d(0, 0);
 * gl.glVertex3d(0, 0, 0);
 * gl.glTexCoord2d(1, 0);
 * gl.glVertex3d(1, 0, 0);
 * gl.glTexCoord2d(1, 1);
 * gl.glVertex3d(1, 1, 0);
 * gl.glTexCoord2d(0, 1);
 * gl.glVertex3d(0, 1, 0);
 * gl.glEnd(); 
 * 
 * Note that it is hard or impossible to texture
 * objects drawn with GLUT. Either define the
 * primitives of the object yourself (as seen
 * above) or add additional textured primitives
 * to the GLUT object.
 */
public class RobotRace extends Base {
    
    /** Array of the four robots. */
    private final Robot[] robots;
    
    /** Instance of the camera. */
    private final Camera camera;
    
    /** Instance of the race track. */
    private final RaceTrack[] raceTracks;
    
    /** Instance of the terrain. */
    private final Terrain terrain;
    
    /**
     * Constructs this robot race by initializing robots,
     * camera, track, and terrain.
     */
    public RobotRace() {
        
        // Create a new array of four robots
        robots = new Robot[4];
        
        // Initialize robot 0
        robots[0] = new Robot(Material.GOLD
            /* add other parameters that characterize this robot */);
        
        // Initialize robot 1
        robots[1] = new Robot(Material.SILVER
            /* add other parameters that characterize this robot */);
        
        // Initialize robot 2
        robots[2] = new Robot(Material.WOOD
            /* add other parameters that characterize this robot */);

        // Initialize robot 3
        robots[3] = new Robot(Material.ORANGE
            /* add other parameters that characterize this robot */);
        
        // Initialize the camera
        camera = new Camera();
        
        // Initialize the race tracks
        raceTracks = new RaceTrack[5];
        
        // Test track
        raceTracks[0] = new RaceTrack();
        
        // O-track
        raceTracks[1] = new RaceTrack(new Vector[] {
            /* add control points like:
            new Vector(10, 0, 1), new Vector(10, 5, 1), new Vector(5, 10, 1),
            new Vector(..., ..., ...), ...
            */
        });
        
        // L-track
        raceTracks[2] = new RaceTrack(new Vector[] { 
            /* add control points */
        });
        
        // C-track
        raceTracks[3] = new RaceTrack(new Vector[] { 
            /* add control points */
        });
        
        // Custom track
        raceTracks[4] = new RaceTrack(new Vector[] { 
           /* add control points */
        });
        

        
        // Initialize the terrain
        terrain = new Terrain();
    }
    
    /**
     * Called upon the start of the application.
     * Primarily used to configure OpenGL.
     */
    @Override
    public void initialize() {	
        // Enable blending.
        gl.glEnable(GL_BLEND);
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                
        // Enable depth testing.
        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LESS);
		
	// Normalize normals.
        gl.glEnable(GL_NORMALIZE);
        
        //Enable smooth shading
        gl.glShadeModel(GL_SMOOTH);
        gl.glEnable(GL_COLOR_MATERIAL);
        gl.glColorMaterial(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE);
        
        // Enable textures. 
        gl.glEnable(GL_TEXTURE_2D);
        gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        gl.glBindTexture(GL_TEXTURE_2D, 0);
		
	// Try to load four textures, add more if you like.
        track = loadTexture("track.jpg");
        brick = loadTexture("brick.jpg");
        head = loadTexture("head.jpg");
        torso = loadTexture("torso.jpg");
        
        
    }
    
    /**
     * Configures the viewing transform.
     */
    @Override
    public void setView() {
        //1.2 viewing
        // Select part of window.
        gl.glViewport(0, 0, gs.w, gs.h);
        
        // Set projection matrix.
        gl.glMatrixMode(GL_PROJECTION);
        gl.glLoadIdentity();

        // Set the perspective.
        // Modify this to meet the requirements in the assignment.
        glu.gluPerspective(Math.toDegrees((2*atan((0.5*gs.vWidth)/gs.vDist))), (float)gs.w / (float)gs.h, 0.1*gs.vDist, 10*gs.vDist);
        // Set camera.
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity();
               
        // Update the view according to the camera mode and robot of interest.
        // For camera modes 1 to 4, determine which robot to focus on.
        camera.update(gs, robots[0]);
        
        //1.4 Shading  
        gl.glEnable(GL_LIGHTING); // Enable lighting
        gl.glEnable(GL_LIGHT0); // Enable light source #0  
        
        float lightAnglePhi = (float) (gs.phi + 10*Math.PI/180) ;
        float lightAngleTheta = (float) (gs.theta - 10*Math.PI/180 );
        
        float x = (float)camera.eye.x()*(float)cos(-toRadians(10)) - (float) camera.eye.y() * (float) sin (-toRadians(10));
        float y = (float)camera.eye.y()*(float)sin(-toRadians(10)) - (float) camera.eye.y() * (float) cos (-toRadians(10));
        float z = (float)camera.eye.z()*(float)tan(-toRadians(10)) * (float) sqrt((x)*(x)+(y)*(y));
        float lightPos[] = {x, y, z, 0f};
        float whiteColor[] = {1.0f, 1.0f, 1.0f, 1.0f};
        float specular[] = {0.5f, 0.5f, 0.5f, 0.5f};
        float globalAmbient[] = {1f, 1f, 1f, 0f};
     
        // position LS 0
        gl.glLightfv(GL_LIGHT0, GL_POSITION, lightPos, 0);
        gl.glLightfv(GL_LIGHT0, GL_DIFFUSE, whiteColor, 0);
        gl.glLightfv(GL_LIGHT0, GL_AMBIENT, globalAmbient, 0);
        gl.glLightfv(GL_LIGHT0, GL_SPECULAR, specular, 0);
    }
    
    /**
     * Draws the entire scene.
     */
    @Override
    public void drawScene() {
           

        // Background color.
        gl.glClearColor(1f, 1f, 1f, 0f);
        

        
        // Clear background.
        gl.glClear(GL_COLOR_BUFFER_BIT);
        
        // Clear depth buffer.
        gl.glClear(GL_DEPTH_BUFFER_BIT);
        
        // Set color to black.
        gl.glColor3f(0f, 0f, 0f);
        
        gl.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        
        

        
        //1.3 Robot
        // Draw the first robot.
        if (gs.showStick) { //All robots are stick figures 
            //robot 0
            robots[0].position = raceTracks[gs.trackNr].getLanePoint(1, (gs.tAnim/40)%1);
            robots[0].direction = raceTracks[gs.trackNr].getLaneTangent(1, (gs.tAnim/40)%1);
            double angle = Math.toDegrees(Math.atan2(robots[0].direction.y(), robots[0].direction.x()));
            gl.glPushMatrix();
            gl.glTranslated(robots[0].position.x, robots[0].position.y, robots[0].position.z);
            gl.glRotated(angle+90, 0, 0, 1);
            robots[0].draw(gl, glu, glut, true, gs.tAnim);
            gl.glPopMatrix();
            
            //robot 1
            robots[1].position = raceTracks[gs.trackNr].getLanePoint(1, ((gs.tAnim/40)%1)+0.05f);
            robots[1].direction = raceTracks[gs.trackNr].getLaneTangent(1, ((gs.tAnim/40)%1)+0.05f);
            double angle2 = Math.toDegrees(Math.atan2(robots[1].direction.y(), robots[1].direction.x()));
            gl.glPushMatrix();
            gl.glTranslated(robots[1].position.x, robots[1].position.y, robots[1].position.z);
            gl.glRotated(angle2+90, 0, 0, 1);
            robots[1].draw(gl, glu, glut, true, gs.tAnim);
            gl.glPopMatrix();
            
            //robot 2
            robots[2].position = raceTracks[gs.trackNr].getLanePoint(1, ((gs.tAnim/40)%1)+0.1f);
            robots[2].direction = raceTracks[gs.trackNr].getLaneTangent(1, ((gs.tAnim/40)%1)+0.1f);
            double angle3 = Math.toDegrees(Math.atan2(robots[2].direction.y(), robots[2].direction.x()));
            gl.glPushMatrix();
            gl.glTranslated(robots[2].position.x, robots[2].position.y, robots[2].position.z);
            gl.glRotated(angle3+90, 0, 0, 1);
            robots[2].draw(gl, glu, glut, true, gs.tAnim);
            gl.glPopMatrix();
            
            //robot 3
            robots[3].position = raceTracks[gs.trackNr].getLanePoint(1, ((gs.tAnim/40)%1)+0.15f);
            robots[3].direction = raceTracks[gs.trackNr].getLaneTangent(1, ((gs.tAnim/40)%1)+0.15f);
            double angle4 = Math.toDegrees(Math.atan2(robots[3].direction.y(), robots[3].direction.x()));
            gl.glPushMatrix();
            gl.glTranslated(robots[3].position.x, robots[3].position.y, robots[3].position.z);
            gl.glRotated(angle4+90, 0, 0, 1);
            robots[3].draw(gl, glu, glut, true, gs.tAnim);
            gl.glPopMatrix();
        } else { // All robots are filled 
            //robot 0
            robots[0].position = raceTracks[gs.trackNr].getLanePoint(1, (gs.tAnim/40)%1);
            robots[0].direction = raceTracks[gs.trackNr].getLaneTangent(1, (gs.tAnim/40)%1);
            double angle = Math.toDegrees(Math.atan2(robots[0].direction.y(), robots[0].direction.x()));
            gl.glPushMatrix();
            gl.glTranslated(robots[0].position.x, robots[0].position.y, robots[0].position.z);
            gl.glRotated(angle+90, 0, 0, 1);
            robots[0].draw(gl, glu, glut, false, gs.tAnim);
            gl.glPopMatrix();
            
            //robot 1
            robots[1].position = raceTracks[gs.trackNr].getLanePoint(1, ((gs.tAnim/40)%1)+0.05f);
            robots[1].direction = raceTracks[gs.trackNr].getLaneTangent(1, ((gs.tAnim/40)%1)+0.05f);
            double angle2 = Math.toDegrees(Math.atan2(robots[1].direction.y(), robots[1].direction.x()));
            gl.glPushMatrix();
            gl.glTranslated(robots[1].position.x, robots[1].position.y, robots[1].position.z);
            gl.glRotated(angle2+90, 0, 0, 1);
            robots[1].draw(gl, glu, glut, false, gs.tAnim);
            gl.glPopMatrix();
            
            //robot 2
            robots[2].position = raceTracks[gs.trackNr].getLanePoint(1, ((gs.tAnim/40)%1)+0.1f);
            robots[2].direction = raceTracks[gs.trackNr].getLaneTangent(1, ((gs.tAnim/40)%1)+0.1f);
            double angle3 = Math.toDegrees(Math.atan2(robots[2].direction.y(), robots[2].direction.x()));
            gl.glPushMatrix();
            gl.glTranslated(robots[2].position.x, robots[2].position.y, robots[2].position.z);
            gl.glRotated(angle3+90, 0, 0, 1);
            robots[2].draw(gl, glu, glut, false, gs.tAnim);
            gl.glPopMatrix();
            
            //robot 3
            robots[3].position = raceTracks[gs.trackNr].getLanePoint(1, ((gs.tAnim/40)%1)+0.15f);
            robots[3].direction = raceTracks[gs.trackNr].getLaneTangent(1, ((gs.tAnim/40)%1)+0.15f);
            double angle4 = Math.toDegrees(Math.atan2(robots[3].direction.y(), robots[3].direction.x()));
            gl.glPushMatrix();
            gl.glTranslated(robots[3].position.x, robots[3].position.y, robots[3].position.z);
            gl.glRotated(angle4+90, 0, 0, 1);
            robots[3].draw(gl, glu, glut, false, gs.tAnim);
            gl.glPopMatrix();
        }
       
        // Draw the race track.
        raceTracks[gs.trackNr].draw(gl, glu, glut);
        
        //Draw some trees
        drawTrees();
        
        // Draw the terrain.
        terrain.draw(gl, glu, glut);
        
        gl.glDisable(GL_LIGHTING); // Enable lighting
        gl.glDisable(GL_LIGHT0); // Enable light source #0
        gl.glDisable(GL_LIGHT1);
        // Draw the axis frame.
        if (gs.showAxes) {
            drawAxisFrame();
        }
                
        gl.glEnable(GL_LIGHTING); // Enable lighting
        gl.glEnable(GL_LIGHT0); // Enable light source #0
        gl.glEnable(GL_LIGHT1); 
        
        //Draw the clock
        drawClock();     
    }
    
    public void drawClock() {
            //This methods is to draw the clock including the date
            //Set the formats for both the time and the date
            SimpleDateFormat TimeFormat = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat DateFormat = new SimpleDateFormat("FF-MM-YYYY");
            
            //This is the first renderer to draw the time with font size 48
            TextRenderer r = new TextRenderer(new Font("Arial", Font.BOLD, 48)); 
            r.beginRendering(gs.w, gs.h); 
            r.setColor(Color.BLACK);
            r.draw(TimeFormat.format(Calendar.getInstance().getTime()), gs.w/2 -60, gs.h-60); 
            r.endRendering(); 
            
            //This is the second renderer to draw the date with font size 20 below the time
            TextRenderer r2 = new TextRenderer(new Font("Arial", Font.BOLD, 20)); 
            r2.beginRendering(gs.w, gs.h); 
            r2.setColor(Color.BLACK);
            r2.draw(DateFormat.format(Calendar.getInstance().getTime()), gs.w/2 -20, gs.h-80); 
            r2.endRendering();
    }
    
    public void drawTrees() {
        drawTree(1f, -15f, -4.3f);
        drawTree(1.2f, -15.7f, -8.3f);
    }
    
    public void drawTree(float size, float x, float y) {
        gl.glPushMatrix();
        gl.glScalef(size,size,size);
        
        //Draw the stem
        gl.glColor3f(0.54f, 0.27f, 0.074f); //brown
        gl.glTranslatef(x, y, 2.5f);
        gl.glScalef(0.4f, 0.4f, 5.0f);
        glut.glutSolidCube(1f);
        gl.glScalef(2.5f,2.5f,0.2f);
        
        //Draw a branch
        gl.glPushMatrix();
        gl.glRotatef(55, 0,1,0);
        gl.glTranslatef(0f, 0f,1f);
        gl.glScalef(0.2f, 0.2f, 2.0f);
        glut.glutSolidCube(1f);
        gl.glPopMatrix();
        
        //Draw a another branch on the opposite side
        gl.glPushMatrix();
        gl.glTranslatef(0f,0f,-0.7f);
        gl.glRotatef(-55, 0,1,0);
        gl.glTranslatef(0f, 0f,1f);
        gl.glScalef(0.2f, 0.2f, 2.0f);
        glut.glutSolidCube(1f);
        gl.glPopMatrix();
        
        //Draw the leaves
        gl.glTranslatef(0,0,2.5f);
        gl.glColor3f(0f, 0.40f, 0f); //green
        glut.glutSolidSphere(1.5f, 20,20);
        
        gl.glPopMatrix();        
    }
    /**
     * Draws the x-axis (red), y-axis (green), z-axis (blue),
     * and origin (yellow).
     */
    public void drawAxisFrame() {
        // 1.1 Axis frame
        
        gl.glColor3f(256f, 256f, 0f);//yellow
        glut.glutSolidSphere(1/8f, 10, 10); // draw sphere, radius 1/8f, longitude and latitude are 10 respectively
        
        // draw x axis
        gl.glColor3f(256f, 0f, 0f); //red
        gl.glTranslatef(0.5f, 0, 0);//moves in x-direction
        gl.glScalef(1f, 0.1f, 0.1f);//scales in vertical direction
        glut.glutSolidCube(1f); //render a solid cube
        gl.glScalef(1f, 10f, 10f);//fill in the cube

        // draw the cone of x axis
        gl.glTranslatef(0.5f, 0, 0);//moves in x-direction
        gl.glRotatef(90, 0, 1, 0); //90 degree rotation
        glut.glutSolidCone(0.15, 0.2, 20, 20); //render a solid cone
        gl.glRotatef(90, 0, -1f, 0);//(0,-1,0)
        gl.glTranslatef(-1f, 0, 0);//(-1,0,0)
        
         // draw y axis
        gl.glColor3f(0f, 256f, 0f); //green
        gl.glTranslatef(0, 0.5f, 0);//moves in y-direction
        gl.glScalef(0.1f, 1f, 0.1f);//scales in vertical direction
        glut.glutSolidCube(1f); //render a solid cube
        gl.glScalef(10f, 1f, 10f);//fill in the cube

        // draw the cone of y axis
        gl.glTranslatef(0, 0.5f, 0);//blah
        gl.glRotatef(90, -1, 0, 0); //90 degree rotation opposite to the normal
        glut.glutSolidCone(0.15, 0.2, 20, 20); //render a solid cone
        gl.glRotatef(90, 1f, 0, 0);//(1,0,0)
        gl.glTranslatef(0, -1f, 0);//(0,-1,0)
        
         // draw z axis
        gl.glColor3f(0f, 0f, 256f); //blue
        gl.glTranslatef(0, 0, 0.5f);//moves in y-direction
        gl.glScalef(0.1f, 0.1f, 1f);//scales in vertical direction
        glut.glutSolidCube(1f); //render a solid cube
        gl.glScalef(10f, 10f, 1f);//fill in the cube

        // draw the cone of z axis
        gl.glTranslatef(0, 0, 0.5f);//blah
        gl.glRotatef(90, 0, 0, 1); //90 degree rotation alongside normal
        glut.glutSolidCone(0.15, 0.2, 20, 20); //render a solid cone
        gl.glRotatef(90, 0, 0, -1f); //(0,0,-1)
        gl.glTranslatef(0, 0, -1f); //(0,0,-1)
        
        
    }
    
    public class Robot {
    
    /** The position of the robot. */
    public Vector position = new Vector(0, 1, 0);
    
    /** The direction in which the robot is running. */
    public Vector direction = new Vector(1, 0, 0);

    /** The material from which this robot is built. */
    private final Material material;

    /**
     * Constructs the robot with initial parameters.
     */
    public Robot(Material material
        /* add other parameters that characterize this robot */) {
        this.material = material;

        // code goes here ...
    }

    /**
     * Draws this robot (as a {@code stickfigure} if specified).
     */
    public void draw(GL2 gl, GLU glu, GLUT glut, boolean stickFigure, float tAnim) {

        if (stickFigure) {
            //MIDDLE
            //Caudale
            gl.glPushMatrix();
            gl.glTranslatef(0, 0, 0.75f);
            glut.glutSolidSphere(1/10f, 10, 10);
            gl.glPopMatrix();

            //connection legs
            gl.glPushMatrix();
            gl.glTranslatef(0, 0, 0.75f);
            gl.glScalef(0.5f, 0.1f, 0.1f);
            glut.glutSolidCube(1f);
            gl.glPopMatrix();

            //spine
            gl.glPushMatrix();
            gl.glTranslatef(0, 0, 1.125f);
            gl.glScalef(0.1f, 0.1f, 0.75f);
            glut.glutSolidCube(1f);
            gl.glPopMatrix();

            //Cervical vertebrea
            gl.glPushMatrix();
            gl.glTranslatef(0, 0, 1.5f);
            glut.glutSolidSphere(1/10f, 10, 10);
            gl.glPopMatrix();

            //wings
            gl.glPushMatrix();
            gl.glTranslatef(0, 0, 1.5f);
            gl.glScalef(1.0f, 0.1f, 0.1f);
            glut.glutSolidCube(1f);
            gl.glPopMatrix();

            //neck
            gl.glPushMatrix();
            gl.glTranslatef(0, 0, 1.65f);
            gl.glScalef(0.1f, 0.1f, 0.3f);
            glut.glutSolidCube(1f);
            gl.glPopMatrix();

            //head
            gl.glPushMatrix();
            gl.glTranslatef(0, 0, 1.9f);
            glut.glutSolidSphere(1/5f, 10, 10);
            gl.glPopMatrix();
            
            //left shoulder
            gl.glPushMatrix();
            gl.glTranslatef(-0.5f, 0, 1.5f);
            glut.glutSolidSphere(1/10f, 10, 10);
            gl.glPopMatrix();
            
            //right shoulder
            gl.glPushMatrix();
            gl.glTranslatef(0.5f, 0, 1.5f);
            glut.glutSolidSphere(1/10f, 10, 10);
            gl.glPopMatrix();


            drawLeftLegStick(gl, glu, glut, 40*(float) cos((tAnim%2-1)*PI));
            drawRightLegStick(gl, glu, glut, -40*(float) cos((tAnim%2-1)*PI));
            
            drawRightArmStick(gl, glu, glut, 40*(float) cos((tAnim%2-1)*PI));
            drawLeftArmStick(gl, glu, glut, -40*(float) cos((tAnim%2-1)*PI)); 
        }
        else {
            //The shading and shiness parameters
            gl.glColor3f(material.diffuse[0], material.diffuse[1],material.diffuse[2]);
            gl.glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, material.specular, 0);
            gl.glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, material.shininess); //The shininess parameter

                              
            //draw legs based on rotation
            drawLeftLeg(gl, glu, glut, 40*(float) cos((tAnim%2-1)*PI));
            drawRightLeg(gl, glu, glut, -40*(float) cos((tAnim%2-1)*PI));
            
            //draw arms based on rotatino opposite of the legs
            drawLeftArm(gl, glu, glut, -40*(float) cos((tAnim%2-1)*PI));
            drawRightArm(gl, glu, glut, 40*(float) cos((tAnim%2-1)*PI));

            
            //head
            gl.glPushMatrix();
            gl.glTranslatef(0, 0, 1.9f);
            glut.glutSolidCube(0.45f);
            gl.glPopMatrix(); 
            //torso
            gl.glPushMatrix();
            gl.glTranslatef(0, 0, 1.125f);
            gl.glScalef(0.5f, 0.3f, 1.0f);
            glut.glutSolidCube(1f);
            gl.glPopMatrix();
            //neck
            gl.glPushMatrix();
            gl.glTranslatef(0, 0, 1.65f);
            gl.glScalef(0.2f, 0.2f, 0.3f);
            glut.glutSolidCube(1f);
            gl.glPopMatrix();  
            //shoulders
            gl.glPushMatrix();
            gl.glTranslatef(0, 0, 1.4f);
            gl.glScalef(1.3f, 0.25f, 0.35f);
            glut.glutSolidCube(1f);
            gl.glPopMatrix();
            
            drawTorsoTexture(gl, glu, glut);
            drawHeadTexture(gl, glu, glut);
            drawBackTexture(gl, glu, glut);
        }
    }
    
    public void drawTorsoTexture(GL2 gl, GLU glu, GLUT glut) {
            // prepare head texture
            gl.glEnable(GL_TEXTURE_2D);
            torso.bind(gl);
            gl.glTexParameteri(GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
            gl.glTexParameteri(GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
            gl.glBegin(GL_QUADS);
                gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-0.25f,  -0.17f,  1.625f);   // Bottom Left Of The Texture and Quad
                gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f( 0.25f,  -0.17f,  1.625f);   // Bottom Right Of The Texture and Quad
                gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( 0.25f,  -0.17f,  0.625f);  // Top Right Of The Texture and Quad
                gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-0.25f,  -0.17f,  0.625f); 
            gl.glEnd(); 
            gl.glDisable(GL_TEXTURE_2D);        
    }
    
    public void drawBackTexture(GL2 gl, GLU glu, GLUT glut) {
            // prepare head texture
            gl.glEnable(GL_TEXTURE_2D);
            torso.bind(gl);
            gl.glTexParameteri(GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
            gl.glTexParameteri(GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
            gl.glBegin(GL_QUADS);
                gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-0.25f,  0.17f,  1.625f);   // Bottom Left Of The Texture and Quad
                gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f( 0.25f,  0.17f,  1.625f);   // Bottom Right Of The Texture and Quad
                gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( 0.25f,  0.17f,  0.625f);  // Top Right Of The Texture and Quad
                gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-0.25f,  0.17f,  0.625f); 
            gl.glEnd(); 
            gl.glDisable(GL_TEXTURE_2D);        
    }    
    
    public void drawHeadTexture(GL2 gl, GLU glu, GLUT glut) {
            // prepare head texture
            gl.glEnable(GL_TEXTURE_2D);
            head.bind(gl);
            gl.glTexParameteri(GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
            gl.glTexParameteri(GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
            gl.glBegin(GL_QUADS);
                gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-0.225f,  -0.227f,  1.675f);   // Bottom Left Of The Texture and Quad
                gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f( 0.225f,  -0.227f,  1.675f);   // Bottom Right Of The Texture and Quad
                gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( 0.225f,  -0.227f,  2.125f);  // Top Right Of The Texture and Quad
                gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-0.225f,  -0.227f,  2.125f); 
            gl.glEnd(); 
            
            gl.glDisable(GL_TEXTURE_2D);
    }

    //These methods are for drawing the arms and legs of the stick robots
    //at a certain rotation.
    public void drawLeftArmStick(GL2 gl, GLU glu, GLUT glut, float rotation) {
            //Set the rotation around the left hip
            gl.glPushMatrix();
            gl.glTranslatef(-0.5f, 0, 1.5f);
            gl.glRotatef(rotation, 1.0f, 0f, 0f);
            gl.glTranslatef(0.5f, 0, -1.5f);
            
            //left arm
            gl.glPushMatrix();
            gl.glTranslatef(-0.5f, 0, 1.1f);
            gl.glScalef(0.1f, 0.1f, 0.8f);
            glut.glutSolidCube(1f);
            gl.glPopMatrix();
            //left hand
            gl.glPushMatrix();
            gl.glTranslatef(-0.5f, 0, 0.7f);
            glut.glutSolidSphere(1/10f, 10, 10);
            gl.glPopMatrix();         
            
            //Pop the rotation matrix
            gl.glPopMatrix();
    }
    
        public void drawRightArmStick(GL2 gl, GLU glu, GLUT glut, float rotation) {
            //Set the rotation around the left hip
            gl.glPushMatrix();
            gl.glTranslatef(0.5f, 0, 1.5f);
            gl.glRotatef(rotation, 1.0f, 0f, 0f);
            gl.glTranslatef(-0.5f, 0, -1.5f);
            
            //right arm
            gl.glPushMatrix();
            gl.glTranslatef(0.5f, 0, 1.1f);
            gl.glScalef(0.1f, 0.1f, 0.8f);
            glut.glutSolidCube(1f);
            gl.glPopMatrix();
            //Right hand
            gl.glPushMatrix();
            gl.glTranslatef(0.5f, 0, 0.7f);
            glut.glutSolidSphere(1/10f, 10, 10);
            gl.glPopMatrix();           
            
            //Pop the rotation matrix
            gl.glPopMatrix();
    }
    
    public void drawRightLegStick(GL2 gl, GLU glu, GLUT glut, float rotation) {
            //Set the rotation around the left hip
            gl.glPushMatrix();
            gl.glTranslatef(-0.25f, 0, 0.75f);
            gl.glRotatef(rotation, 1.0f, 0f, 0f);
            gl.glTranslatef(0.25f, 0, -0.75f);
            
            //Right feet
            gl.glPushMatrix();
            gl.glTranslatef(0.25f, 0, 0);
            glut.glutSolidSphere(1/10f, 10, 10);
            gl.glPopMatrix();

            //right upperleg
            gl.glPushMatrix();
            gl.glTranslatef(0.25f, 0, 0.5625f);
            gl.glScalef(0.1f, 0.1f, 0.375f);
            glut.glutSolidCube(1f);
            gl.glPopMatrix();

            //right hip
            gl.glPushMatrix();
            gl.glTranslatef(0.25f, 0, 0.75f);
            glut.glutSolidSphere(1/10f, 10, 10);
            gl.glPopMatrix();
            
            //right knee
            gl.glPushMatrix();
            gl.glTranslatef(0.25f, 0, 0.375f);
            glut.glutSolidSphere(1/10f, 10, 10);
            gl.glPopMatrix();
            
            //right lowerleg
            gl.glPushMatrix();
            gl.glTranslatef(0.25f, 0, 0.1875f);
            gl.glScalef(0.1f, 0.1f, 0.375f);
            glut.glutSolidCube(1f);
            gl.glPopMatrix();
            
            //Pop the rotation matrix
            gl.glPopMatrix();
    }    
    
    public void drawLeftLegStick(GL2 gl, GLU glu, GLUT glut, float rotation) {
            //Set the rotation around the left hip
            gl.glPushMatrix();
            gl.glTranslatef(-0.25f, 0, 0.75f);
            gl.glRotatef(rotation, 1.0f, 0f, 0f);
            gl.glTranslatef(0.25f, 0, -0.75f);
            
            //LEFT SIDE
            //left feet
            gl.glPushMatrix();
            gl.glTranslatef(-0.25f, 0, 0);
            glut.glutSolidSphere(1/10f, 10, 10);
            gl.glPopMatrix();

            //left upperleg
            gl.glPushMatrix();
            gl.glTranslatef(-0.25f, 0, 0.5625f);
            gl.glScalef(0.1f, 0.1f, 0.375f);
            glut.glutSolidCube(1f);
            gl.glPopMatrix();

            //left hip
            gl.glPushMatrix();
            gl.glTranslatef(-0.25f, 0, 0.75f);
            glut.glutSolidSphere(1/10f, 10, 10);
            gl.glPopMatrix();
            
            //left knee
            gl.glPushMatrix();
            gl.glTranslatef(-0.25f, 0, 0.375f);
            glut.glutSolidSphere(1/10f, 10, 10);
            gl.glPopMatrix();
            
            //left lowerleg
            gl.glPushMatrix();
            gl.glTranslatef(-0.25f, 0, 0.1875f);
            gl.glScalef(0.1f, 0.1f, 0.375f);
            glut.glutSolidCube(1f);
            gl.glPopMatrix();
            
            //Pop the rotation matrix
            gl.glPopMatrix();
    }
    
    //These methods are for drawing the arms and legs of the filled robots
    //at a certain rotation.
    public void drawRightArm(GL2 gl, GLU glu, GLUT glut, float rotation) {
        //right arm
        gl.glPushMatrix();
        gl.glTranslatef(0.5f, 0, 1.5f);
        gl.glRotatef(rotation, 1.0f, 0f, 0f);
        gl.glTranslatef(0,0,-0.4f);
        gl.glScalef(0.25f, 0.25f, 0.8f);
        glut.glutSolidCube(1f);
        gl.glPopMatrix();
    }
    
    public void drawLeftArm(GL2 gl, GLU glu, GLUT glut, float rotation) {
        //left arm
        gl.glPushMatrix();        
        gl.glTranslatef(-0.5f, 0, 1.5f);
        gl.glRotatef(rotation, 1.0f, 0f, 0f);
        gl.glTranslatef(0,0,-0.4f);
        gl.glScalef(0.25f, 0.25f, 0.8f);
        glut.glutSolidCube(1f);
        gl.glPopMatrix();
    }
    
    public void drawRightLeg(GL2 gl, GLU glu, GLUT glut, float rotation) {  
        gl.glPushMatrix();
        //This is the position of the hip
        gl.glTranslatef(0.25f, 0, 0.75f);
        //Rotate the upper leg (including the lower leg) with respect to the hip
        gl.glRotatef(rotation, 1.0f, 0f, 0f);
        //Give the upper leg the right dimensions
        gl.glTranslatef(0, 0, -0.1875f);        
        gl.glPushMatrix();
        //Push the scaling so that it can be popped after drawing 
        //and scale and draw the lower leg
        gl.glScalef(0.25f, 0.25f, 0.475f);
        glut.glutSolidCube(1f);
        gl.glPopMatrix();
        //Translate to the end of the upperleg to get ready for the lowerleg
        gl.glTranslatef(0, 0, -0.1875f);
        //translate to the center of where the lowerleg is supposed to be
        gl.glTranslatef(0f, 0f, -0.1875f);
        //Scale and draw the lower leg
        gl.glScalef(0.25f, 0.25f, 0.375f);
        glut.glutSolidCube(1f);  
        gl.glPopMatrix();    
    }
    
    public void drawLeftLeg(GL2 gl, GLU glu, GLUT glut, float rotation) {  
        
        gl.glPushMatrix();
        //This is the position of the hip
        gl.glTranslatef(-0.25f, 0, 0.75f);
        //Rotate the upper leg (including the lower leg) with respect to the hip
        gl.glRotatef(rotation, 1.0f, 0f, 0f);
        //Give the upper leg the right dimensions
        gl.glTranslatef(0, 0, -0.1875f);        
        //Push the scaling so that it can be popped after drawing 
        //and scale and draw the lower leg
        gl.glPushMatrix();
        gl.glScalef(0.25f, 0.25f, 0.475f);
        glut.glutSolidCube(1f);
        gl.glPopMatrix();
        //Translate to the end of the upperleg to get ready for the lowerleg
        gl.glTranslatef(0, 0, -0.1875f);
        //translate to the center of where the lowerleg is supposed to be
        gl.glTranslatef(0f, 0f, -0.1875f);
        //Scale and draw the lower leg
        gl.glScalef(0.25f, 0.25f, 0.375f);
        glut.glutSolidCube(1f);  
        gl.glPopMatrix();    
    }
}
    



    public enum Material {

    /** 
     * Gold material properties.
     * Modify the default values to make it look like gold.
     */
    //We found the properties from http://devernay.free.fr/cours/opengl/materials.html
    GOLD(new float[]{0.75164f, 0.60648f, 0.22648f, 1.0f},
         new float[]{0.628281f, 0.555802f, 0.366065f, 1.0f},
         0.4f * 128),

    /**
     * Silver material properties.
     * Modify the default values to make it look like silver.
     */
    SILVER(new float[]{0.50754f, 0.50754f, 0.50754f, 1.0f},
           new float[]{0.508273f, 0.508273f, 0.508273f, 1.0f},
           0.4f * 128),

    /** 
     * Wood material properties.
     * Modify the default values to make it look like wood.
     */
    WOOD ( //After trying we found the 'bronze' color is closed to the color of wood
        new float[]{0.714f, 0.4284f, 0.18144f, 1.0f},
        new float[]{0.393548f, 0.271906f, 0.166721f, 1.0f},
        0.0f * 128),

    /**
     * Orange material properties.
     * Modify the default values to make it look like orange.
     */
    ORANGE(new float[]{0.5f, 0.25f, 0f, 1.0f},
           new float[]{0.65f, 0.6f, 0.55f, 1.0f},
           0.1f * 128);


    /** The diffuse RGBA reflectance of the material. */
    float[] diffuse;

    /** The specular RGBA reflectance of the material. */
    float[] specular;
    
    /** The specular exponent of the material. */
    float shininess;

    /**
     * Constructs a new material with diffuse and specular properties.
     */
    private Material(float[] diffuse, float[] specular, float shininess) {
        this.diffuse = diffuse;
        this.specular = specular;
        this.shininess = shininess;
    }
}
    
    public class RaceTrack {
    
    /** The width of one lane. The total width of the track is 4 * laneWidth. */
    private final static float laneWidth = 1.22f;

    /** Array with 3N control points, where N is the number of segments. */
    private Vector[] controlPoints = null;
    
    private Vector[] controlPointsOTrack;
    private Vector[] controlPointsLTrack;
    private Vector[] controlPointsCTrack;
    private Vector[] controlPointsCustomTrack;
    
    private ArrayList<Vector> pointsOTrack, tangentOTrack;
    private ArrayList<Vector> pointsLTrack, tangentLTrack;
    private ArrayList<Vector> pointsCTrack, tangentCTrack;
    private ArrayList<Vector> pointsCustomTrack, tangentCustomTrack;

    /**
     * Constructor for the default track.
     */
    public RaceTrack() {
    }
    
    // track the current points and current tangents so we can use them for robot movement
    private ArrayList<Vector> currentPoints, currentTangents;
    
    /**
     * Constructor for a spline track.
     */
    public RaceTrack(Vector[] controlPoints) {
        this.controlPoints = controlPoints;
        this.controlPointsOTrack = new Vector[]{ // each vector represent a part of the track which will be connected lateron
                new Vector(15, 0, 1),
                new Vector(15, 25, 1),
                new Vector(-15, 25, 1),
                new Vector(-15, 0, 1),
                new Vector(-15, -25, 1),
                new Vector(15, -25, 1),
                new Vector(15, 0, 1)

            };
        this.controlPointsLTrack = new Vector[]{ // each vector represent a part of the track which will be connected lateron
                new Vector(-20, 15, 1), // first
                new Vector(-20, -20, 1),
                new Vector(-25, -20, 1),
                new Vector(-15, -20, 1),// second
                new Vector(20, -20, 1),
                new Vector(20, -20, 1),
                new Vector(20, -15, 1),// third
                new Vector(20, -5, 1),
                new Vector(-10, -15, 1),
                new Vector(-10, -5, 1),// fourth
                new Vector(-5, 30, 1),
                new Vector(-20, 25, 1),
                new Vector(-20, 15, 1),

            };
        this.controlPointsCTrack = new Vector[]{
                new Vector(0, 20, 1),
                new Vector(-20, 20, 1),
                new Vector(-20, -20, 1),
                new Vector(0, -20, 1),
                new Vector(12, -20, 1),
                new Vector(12, -12, 1),
                new Vector(5, -12, 1),
                new Vector(-12, -12, 1),
                new Vector(-12, 12, 1),
                new Vector(5, 12, 1),
                new Vector(12, 12, 1),
                new Vector(12, 20, 1),
                new Vector(0, 20, 1)
            };
        this.controlPointsCustomTrack = new Vector[]{
                new Vector(15, 0, 10),
                new Vector(15, 25, 1),
                new Vector(-15, 25, 1),
                new Vector(-15, 0, 1),
                new Vector(-15, -25, 10),
                new Vector(15, -25, 10),
                new Vector(15, 0, 10)
            };
        
            // pre calculate all points for O track
            pointsOTrack = getAllPoints(controlPointsOTrack);
            tangentOTrack = getAllTangent(controlPointsOTrack);

            // pre calculate all points for L track
            pointsLTrack = getAllPoints(controlPointsLTrack);
            tangentLTrack = getAllTangent(controlPointsLTrack);
            
            // pre calculate all points for C track
            pointsCTrack = getAllPoints(controlPointsCTrack);
            tangentCTrack = getAllTangent(controlPointsCTrack);

            // pre calculate all points for custom track
            pointsCustomTrack = getAllPoints(controlPointsCustomTrack);
            tangentCustomTrack = getAllTangent(controlPointsCustomTrack);
    }

    /**
     * Draws this track, based on the control points.
     */
    public void draw(GL2 gl, GLU glu, GLUT glut) {
        if (null == controlPoints) {
            ArrayList<Vector> points = new ArrayList<Vector>();
            ArrayList<Vector> tangents = new ArrayList<Vector>();

            for (int i = 0; i <= 1000; i++) { // load the track points
                double par = i / 1000f;
                points.add(getPoint(par));
                tangents.add(getTangent(par));
            }

            gl.glColor3f(Material.SILVER.diffuse[0], Material.SILVER.diffuse[1], Material.SILVER.diffuse[2]); // use a silver material for the lines

            drawRing(1, points, tangents); // draw each platform
            drawRing(2, points, tangents);
            drawRing(3, points, tangents);
            drawRing(4, points, tangents);

            drawSides(points, tangents); // draw the sides of the track

            if (currentPoints != points) { // set the current track points to this track, if not done already
                currentPoints = points;
            }
            if (currentTangents != tangents) { // do the same for the tangents
                currentTangents = tangents;
            }
        } else {
            // draw the spline track
            //O track
            if(gs.trackNr == 1){
                gl.glColor3f(Material.SILVER.diffuse[0], Material.SILVER.diffuse[1], Material.SILVER.diffuse[2]);
                drawRing(1, pointsOTrack, tangentOTrack); // same steps but then for the O track
                drawRing(2, pointsOTrack, tangentOTrack);
                drawRing(3, pointsOTrack, tangentOTrack);
                drawRing(4, pointsOTrack, tangentOTrack);

                drawSides(pointsOTrack, tangentOTrack);

                if (currentPoints != pointsOTrack) {
                    currentPoints = pointsOTrack;
                }
                if (currentTangents != tangentOTrack) {
                    currentTangents = tangentOTrack;
                }

        }
            //L track
            if (gs.trackNr == 2) {
                gl.glColor3f(Material.SILVER.diffuse[0], Material.SILVER.diffuse[1], Material.SILVER.diffuse[2]);
                drawRing(1, pointsLTrack, tangentLTrack);
                drawRing(2, pointsLTrack, tangentLTrack);
                drawRing(3, pointsLTrack, tangentLTrack);
                drawRing(4, pointsLTrack, tangentLTrack);

                drawSides(pointsLTrack, tangentLTrack);

                if (currentPoints != pointsLTrack) {
                    currentPoints = pointsLTrack;
                }
                if (currentTangents != tangentLTrack) {
                    currentTangents = tangentLTrack;
                }
        }
            
            //C track
            if (gs.trackNr == 3) {
                gl.glColor3f(Material.SILVER.diffuse[0], Material.SILVER.diffuse[1], Material.SILVER.diffuse[2]);
                drawRing(1, pointsCTrack, tangentCTrack);
                drawRing(2, pointsCTrack, tangentCTrack);
                drawRing(3, pointsCTrack, tangentCTrack);
                drawRing(4, pointsCTrack, tangentCTrack);

                drawSides(pointsCTrack, tangentCTrack);

                if (currentPoints != pointsCTrack) {
                    currentPoints = pointsCTrack;
                }
                if (currentTangents != tangentCTrack) {
                    currentTangents = tangentCTrack;
                }
        }
            
        //Custom track
            if (gs.trackNr == 4) {
                gl.glColor3f(Material.SILVER.diffuse[0], Material.SILVER.diffuse[1], Material.SILVER.diffuse[2]);
                drawRing(1, pointsCustomTrack, tangentCustomTrack);
                drawRing(2, pointsCustomTrack, tangentCustomTrack);
                drawRing(3, pointsCustomTrack, tangentCustomTrack);
                drawRing(4, pointsCustomTrack, tangentCustomTrack);

                drawSides(pointsCustomTrack, tangentCustomTrack);

                if (currentPoints != pointsCustomTrack) {
                    currentPoints = pointsCustomTrack;
                }
                if (currentTangents != tangentCustomTrack) {
                    currentTangents = tangentCustomTrack;
                }
        }
    }
 }
    
    /**
     * Returns the center of a lane at 0 <= t < 1.
     * Use this method to find the position of a robot on the track.
     */
    public Vector getLanePoint(int lane, double t) {
        if (null == controlPoints) {
            return getPoint(t); // <- code goes here
        } else {
            return Vector.O; // <- code goes here
        }
    }
    
    /**
     * Returns the tangent of a lane at 0 <= t < 1.
     * Use this method to find the orientation of a robot on the track.
     */
    public Vector getLaneTangent(int lane, double t) {
        if (null == controlPoints) {
            Vector robotTangent = getTangent(t);
            return robotTangent; // <- code goes here
        } else {
            return Vector.O; // <- code goes here
        }
    }

    /**
     * Returns a point on the test track at 0 <= t < 1.
     */
    private Vector getPoint(double t) {
        return new Vector(10 * cos(2 * PI * t), 14 * sin(2 * PI * t),1);
    }

    /**
     * Returns a tangent on the test track at 0 <= t < 1.
     */
    private Vector getTangent(double t) {
        return new Vector(-20 * PI * sin(2 * PI * t), 28 * PI * cos(2 * PI * t), 1);
    }
    
      // generate a arraylist with all tangents using cubicbezier
        public ArrayList<Vector> getAllTangent(Vector[] points) {
            ArrayList<Vector> array = new ArrayList<Vector>();
            for (int z = 0; z < points.length - 3; z += 3) {
                for (int i = 0; i <= 1000; i++) {
                    double t = i / 1000f;
                    Vector point = getCubicBezierTangent(t, points[z], points[z + 1], points[z + 2], points[z + 3]);
                    array.add(point);
                }
            }
            return array;
        }

        // generate a arraylist with points of full track
        public ArrayList<Vector> getAllPoints(Vector[] points) {
            ArrayList<Vector> array = new ArrayList<Vector>();
            for (int z = 0; z < points.length - 3; z += 3) {
                for (int i = 0; i <= 1000; i++) {
                    double t = i / 1000f;
                    Vector point = getCubicBezierPoint(t, points[z], points[z + 1], points[z + 2], points[z + 3]);
                    array.add(point);
                }
            }
            return array;
        }
    
    /**
     * Returns a point on a bezier segment with control points
     * P0, P1, P2, P3 at 0 <= t < 1.
     */
    private Vector getCubicBezierPoint(double t, Vector P0, Vector P1,
                                                 Vector P2, Vector P3) {
        Vector first = P0.scale((1 - t) * (1 - t) * (1 - t));
        Vector second = P1.scale(3 * t * (1 - t) * (1 - t));
        Vector third = P2.scale(3 * t * t * (1 - t));
        Vector fourth = P3.scale(t * t * t);
        return first.add(second).add(third).add(fourth);
    }
    
    /**
     * Returns a tangent on a bezier segment with control points
     * P0, P1, P2, P3 at 0 <= t < 1.
     */
    private Vector getCubicBezierTangent(double t, Vector P0, Vector P1,
                                                   Vector P2, Vector P3) {
        Vector first = P1.subtract(P0).scale(3 * (1 - t) * (1 - t));
        Vector second = P2.subtract(P1).scale(6 * (1 - t) * t);
        Vector third = P3.subtract(P2).scale(3 * t * t);
        return first.add(second).add(third);
    }
    
    public void drawRing(int nr, ArrayList<Vector> points, ArrayList<Vector> tangents) { // draw the top part of the track

        // prepare track texture
        gl.glEnable(GL_TEXTURE_2D);
        track.bind(gl);
        gl.glTexParameteri(GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
        gl.glTexParameteri(GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);

        gl.glBegin(GL.GL_TRIANGLE_STRIP);

        for (int i = 0; i < points.size(); i++) {// connect the points using a single triangle strip 
            Vector point = points.get(i);

            Vector tangent = tangents.get(i);
            // the out vector, with length 1
            Vector out = tangent.cross(Vector.Z).normalized();
            Vector shift = out.scale(nr - 3);

            Vector startPoint = point.add(shift);
            Vector endPoint = startPoint.add(out);

            gl.glTexCoord2d(0, i * 100f / 1000);
            gl.glVertex3d(startPoint.x(), startPoint.y(), startPoint.z());

            gl.glTexCoord2d(1, i * 100f / 1000);
            gl.glVertex3d(endPoint.x(), endPoint.y(), endPoint.z());
            gl.glNormal3d(0, 0, 1);

        }
        gl.glEnd();

        gl.glDisable(GL_TEXTURE_2D);

    }
    
    public void drawSides(ArrayList<Vector> points, ArrayList<Vector> tangents) { // draws the sides of the track

            // prepare brick texture
            gl.glEnable(GL_TEXTURE_2D);
            brick.bind(gl);
            gl.glTexParameteri(GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
            gl.glTexParameteri(GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);

            // outer side of the track
            gl.glBegin(GL.GL_TRIANGLE_STRIP);
            for (int i = 0; i < points.size(); i++) { // the points inside the array are now being connected into a single quad strip
                Vector point = points.get(i);

                Vector tangent = tangents.get(i);
                // the out vector, with length 1
                Vector normal = tangent.cross(Vector.Z).normalized();
                Vector out = normal.scale(2);

                Vector startPoint = point.add(out);

                gl.glTexCoord2d(0, i * 100f / 4000);   // map the texture
                gl.glVertex3d(startPoint.x(), startPoint.y(), startPoint.z());

                gl.glTexCoord2d(1, i * 100f / 4000);
                gl.glVertex3d(startPoint.x(), startPoint.y(), startPoint.z() - 2);
                gl.glNormal3d(normal.x(), normal.y(), normal.z());
            }
            gl.glEnd();
            gl.glDisable(GL_TEXTURE_2D);

            // same steps for the inner side of the track
            gl.glEnable(GL_TEXTURE_2D);
            brick.bind(gl);
            gl.glTexParameteri(GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
            gl.glTexParameteri(GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);

            // inner side
            gl.glBegin(GL.GL_TRIANGLE_STRIP);
            for (int i = 0; i < points.size(); i++) {
                Vector point = points.get(i);

                Vector tangent = tangents.get(i);
                Vector normal = Vector.Z.cross(tangent).normalized();
                Vector out = normal.scale(2);

                Vector startPoint = point.add(out);

                gl.glTexCoord2d(0, i * 100f / 4000);
                gl.glVertex3d(startPoint.x(), startPoint.y(), startPoint.z());

                gl.glTexCoord2d(1, i * 100f / 4000);
                gl.glVertex3d(startPoint.x(), startPoint.y(), startPoint.z() - 2);

                gl.glNormal3d(normal.x(), normal.y(), normal.z());
            }
            gl.glEnd();
            gl.glDisable(GL_TEXTURE_2D);
        }
}
    public class Camera {
        /** The position of the camera. */
    public Vector eye = new Vector(3f, -6f, 5f);

    /** The point to which the camera is looking. */
    public Vector center = Vector.O;

    /** The up vector. */
    public Vector up = Vector.Z;
    
    private int mode;

    
    /**
     * Updates the camera viewpoint and direction based on the
     * selected camera mode.
     */
    public void update(GlobalState gs, RobotRace.Robot focus) {
        //System.out.println(gs.vDist);
        switch (gs.camMode) {
            
            // Helicopter mode
            case 1:
                setHelicopterMode(gs, focus);
                break;
                
            // Motor cycle mode    
            case 2:
                setMotorCycleMode(gs, focus);
                break;
                
            // First person mode    
            case 3:
                setFirstPersonMode(gs, focus);
                break;
                
            // Auto mode    
            case 4:
                setAutoMode(gs, focus);
                break;
                
            // Default mode    
            default:
                setDefaultMode(gs);
        }
    }

    /**
     * Computes eye, center, and up, based on the camera's default mode.
     */
    private void setDefaultMode(GlobalState gs) {
            // calculate the eye point
            Vector eyeVector = new Vector(
                    gs.vDist * cos(gs.phi) * cos(gs.theta) + gs.cnt.x(),
                    gs.vDist * cos(gs.phi) * sin(gs.theta) + gs.cnt.y(),
                    gs.vDist * sin(gs.phi) + gs.cnt.z());

            // Update the view according to the camera mode
            //glu.gluLookAt(eyeVector.x(), eyeVector.y(), eyeVector.z(),
              //      gs.cnt.x(), gs.cnt.y(), gs.cnt.z(),
                //    up.x(), up.y(), up.z());
            float cameraX = (float)(gs.vDist*cos(gs.phi)*cos(gs.theta));
            float cameraY = (float)(gs.vDist*cos(gs.phi)*sin(gs.theta));
            float cameraZ = (float)(gs.vDist*sin(gs.phi));
            eye = new Vector(cameraX, cameraY, cameraZ);
            
            glu.gluLookAt(cameraX, cameraY, cameraZ,
                      gs.cnt.x, gs.cnt.y, gs.cnt.z,
                      0,     0,     1);
            
//            float cameraX = (float)(gs.vDist*cos(gs.phi)*cos(gs.theta));
//            float cameraY = (float)(gs.vDist*cos(gs.phi)*sin(gs.theta));
//            float cameraZ = (float)(gs.vDist*sin(gs.phi));
//            camera.eye = new Vector(cameraX, cameraY, cameraZ);
    }

    /**
     * Computes eye, center, and up, based on the helicopter mode.
     * The camera should focus on the robot.
     */
    private void setHelicopterMode(GlobalState gs, RobotRace.Robot focus) {
          RobotRace.Robot trackingRobot = robots[0];
            // follow the robot that is nearest to finish line
            

            // set the eyevector to the tracking robot position and add some z
            Vector eyeVector = new Vector(
                    trackingRobot.position.x(),
                    trackingRobot.position.y(),
                    trackingRobot.position.z()+20);
            eye = eyeVector;
            
            Vector x = raceTracks[gs.trackNr].getLaneTangent(1, (gs.tAnim/40)%1);
            
            // calculate the up vector with help of the tangent
            Vector newUp = new Vector(
                    x.normalized().x,
                    x.normalized().y,
                    0);

            // Update the view according to the camera mode
            glu.gluLookAt(eyeVector.x(), eyeVector.y(), eyeVector.z(),
                    trackingRobot.position.x(), trackingRobot.position.y(), trackingRobot.position.z(),
                    newUp.x(), newUp.y(), newUp.z());
            //change the Vdist and width in default
            gs.vDist = 10;
            gs.vWidth = 15;
            //System.out.println(gs.vDist);
    }

    /**
     * Computes eye, center, and up, based on the motorcycle mode.
     * The camera should focus on the robot.
     */
    private void setMotorCycleMode(GlobalState gs, RobotRace.Robot focus) {
            RobotRace.Robot trackingRobot = robots[0];
            

            // get robot direction and normalize it
            Vector robotTangent = raceTracks[0].getLaneTangent(1, (gs.tAnim/40)%1);
            // cross product with (0,0,1), so we know what direction and finally scale a bit scale
            Vector eyeVector = robotTangent.cross(up).scale(2);
            eye = eyeVector;
            // Update the view according to the camera mode
            glu.gluLookAt(
                    trackingRobot.position.x() + eyeVector.x(),
                    trackingRobot.position.y() + eyeVector.y(),
                    trackingRobot.position.z() + 2,
                    trackingRobot.position.x(), trackingRobot.position.y(),
                    trackingRobot.position.z(),
                    0, 0, 1);
            gs.vDist = 100;
            gs.vWidth = 10;
    }         
    /**
     * Computes eye, center, and up, based on the first person mode.
     * The camera should view from the perspective of the robot.
     */
    private void setFirstPersonMode(GlobalState gs, RobotRace.Robot focus) {
        RobotRace.Robot trackingRobot = robots[0];
        // get robot direction and normalize it
        Vector robotTangent = raceTracks[gs.trackNr].getLaneTangent(1, (gs.tAnim/40)%1);
        // cross product with (0,0,1), so we know what direction and finally scale a bit scale
        Vector eyeVector = robotTangent.cross(up).scale(10);

        // Update the view according to the camera mode
        glu.gluLookAt(trackingRobot.position.x(),
                trackingRobot.position.y(),
                trackingRobot.position.z() + 2,
                robotTangent.x(), robotTangent.y(),
                robotTangent.z(),
                0, 0, 1);
        gs.vDist = 10;
        gs.vWidth = 15;
    }
    
    /**
     * Computes eye, center, and up, based on the auto mode.
     * The above modes are alternated.
     */
    private void setAutoMode(GlobalState gs, RobotRace.Robot focus) {



        
    }
    


}
    public class Terrain {
        
    private float dt;

    
    public Terrain() {
        dt = 1f;
    }

        /**
         * Draws the terrain.
        */
        public void draw(GL2 gl, GLU glu, GLUT glut) {
            gl.glColor3f(0, 0, 0);
            for (int y = -25; y <= 25; y += dt) {
                gl.glBegin(gl.GL_QUAD_STRIP);
                for (int x = -25; x <= 25; x += dt) {
                    Vector normal = calculateNormalPerVertex(x, y);
                    gl.glNormal3d(normal.x(), normal.y(), normal.z());
                    setSurfaceColor(heightAt(x, y));
                    gl.glVertex3f(x, y, heightAt(x, y));
                    normal = calculateNormalPerVertex(x, y + dt);
                    gl.glNormal3d(normal.x(), normal.y(), normal.z());
                    setSurfaceColor(heightAt(x, y + dt));
                    gl.glVertex3f(x, y + dt, heightAt(x, y + dt));

                }
                gl.glEnd();
        }
               // draw transparent polygon for water effect
                gl.glColor4f(70 / 255f, 130 / 255f, 180 / 255f, 0.4f);
                gl.glBegin(gl.GL_QUAD_STRIP);
                gl.glVertex3f(-25f, -25f, 0);
                gl.glVertex3f(-25f, 25, 0);
                gl.glVertex3f(25f, -25f, 0);
                gl.glVertex3f(25f, 25f, 0);
                gl.glEnd();
        }
        // finite difference method (link:
        // http://stackoverflow.com/questions/13983189/opengl-how-to-calculate-normals-in-a-terrain-height-grid_
        // this is used since you easily can change the z formula without recalculating the differentials.
        // this is only a approxiamation, but it is sufficient for this model
        public Vector calculateNormalPerVertex(float x, float y) {
            float offset = 1;
            // calculate normals on for each vertex
            float hL = heightAt(x - offset, y);
            float hR = heightAt(x + offset, y);
            float hD = heightAt(x, y - offset);
            float hU = heightAt(x, y + offset);

            return (new Vector(hL - hR, hD - hU, 2.0f)).normalized();
        }
 
        /**
        * Computes the elevation of the terrain at (x, y).
        */
        public float heightAt(float x, float y) {
            double z = 0.6f * cos(0.01f * (0.2*x) - 0.5f * (0.2*y)) + 0.4f * cos(0.2*x + 0.1f * y);
            return (float) z;
        }
        
        private void setSurfaceColor(double z) {
            // below zero blue (water)
            if (z < -0.2) {
                  gl.glColor3f(0.6f, 1.0f, 0.8f);
            } // from 0 to 0.5 meter yellow (sand),
            else if (z >= -0.2 && z < 0.5) {
                  gl.glColor3f(1.0f, 1.0f, 0.6f);
            } // Else the colour will be green
            else {
                gl.glColor3f(0.0f, 0.6f, 0.0f);
            }
    }
}
    
    /**
     * Main program execution body, delegates to an instance of
     * the RobotRace implementation.
     */
    public static void main(String args[]) {
        RobotRace robotRace = new RobotRace();
        robotRace.run();
    } 

    private void setColor() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
