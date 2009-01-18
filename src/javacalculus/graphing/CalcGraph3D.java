/**
 * 
 */
package javacalculus.graphing;

import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Material;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TriangleStripArray;
import javax.swing.JApplet;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.Stripifier;
import com.sun.j3d.utils.geometry.Triangulator;
import com.sun.j3d.utils.universe.SimpleUniverse;

/**
 * @author Duyun Chen <A HREF="mailto:duchen@seas.upenn.edu">[duchen@seas.upenn.edu]</A>,
 * Seth Shannin <A HREF="mailto:sshannin@seas.upenn.edu">[sshannin@seas.upenn.edu]</A>
 *  
 *
 */
public class CalcGraph3D extends JApplet implements Runnable {
	
	private static final long serialVersionUID = 7066480322477204662L;
	
	private Vector3f lightDirection = new Vector3f(0.0f, 0.0f, -1.0f);
	private Color3f lightColor = new Color3f(0.5f, 0.5f, 0.5f); 
	private Color3f graphColor = new Color3f(1.0f, 0.0f, 0.0f);
	
    //a bounding sphere specifies a region a behavior is active
    //create a sphere centered at the origin with radius of 1.0f
    private BoundingSphere bounds = new BoundingSphere();
	private CalcPlotter3D plotter;
    private float[] points;
    private Color3f[] colors;
    private int[] stripCounts = new int[1];
    
    private float 
    xmin = -1.0f, xmax = 1.0f, ymin = -1.0f, ymax = 1.0f;
    
    private float resolution = 0.1f;
    	
	/**
	 * Constructor
	 */
	public CalcGraph3D(CalcPlotter3D plotter) {
    	this.plotter = plotter;
    	this.points = new float[3*(int)((xmax - xmin)*(ymax - ymin)/(resolution*resolution))+3];
    	this.stripCounts[0] = (int)((xmax - xmin)*(ymax - ymin)/(resolution*resolution)+1);
    	this.colors = new Color3f[((int)((xmax - xmin)*(ymax - ymin)/(resolution*resolution)))+1];
    	populatePoints();
		EventQueue.invokeLater(this);
	}
	
	public void run() {
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();

        Canvas3D canvas3D = new Canvas3D(config);

        BranchGroup scene = createSceneBranchGroup();
        scene.compile();
        
        //Probably use a more sophisticated universe in the future
        SimpleUniverse simpleU = new SimpleUniverse(canvas3D);

		//This will move the ViewPlatform back a bit so the
		//objects in the scene can be viewed.
        simpleU.getViewingPlatform().setNominalViewingTransform();

        simpleU.addBranchGraph(scene);
        
        add("Center", canvas3D);
        
		Frame f = new MainFrame(this, 500, 500);
		f.setTitle("PLOT3D");
	}
	
	private void populatePoints() {
		float x = xmin, y = ymin;
		int index = -1;
		int colorIndex = -1;
		Color3f gradient = graphColor;
		
		while (x < xmax) {
			y = ymin;
			gradient = graphColor;
			while (y < ymax) {
				points[++index] = x;
				points[++index] = y;
				points[++index] = (float) plotter.getZValue(x, y);
				gradient.add(new Color3f(0.0f, 0.01f, 0.01f));
				colors[++colorIndex] = gradient;
				y += resolution;
			}
			x += resolution;
		}
	}
	
	private BranchGroup createSceneBranchGroup() {
		BranchGroup rootGroup = new BranchGroup();

		TransformGroup rotateGroup = new TransformGroup();
		rotateGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		rotateGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		MouseRotate mouseRotate = new MouseRotate();
		mouseRotate.setTransformGroup(rotateGroup);
		mouseRotate.setSchedulingBounds(bounds);
		
		MouseZoom mouseZoom = new MouseZoom();
		mouseZoom.setTransformGroup(rotateGroup);
		mouseZoom.setSchedulingBounds(bounds);		
		
		rootGroup.addChild(mouseRotate);
		
		rootGroup.addChild(mouseZoom);
		
		rotateGroup.addChild(createLighting());
		
		rotateGroup.addChild(createAmbientLighting());
		
		rotateGroup.addChild(createGraph());
		
		rootGroup.addChild(rotateGroup);
		
		return rootGroup;
	}
	
	private Shape3D createGraph() {
		Shape3D graph = new Shape3D();
		
		GeometryInfo graphGeometry = new GeometryInfo(GeometryInfo.POLYGON_ARRAY);
		graphGeometry.setCoordinates(points);
		graphGeometry.setStripCounts(stripCounts);
		graphGeometry.setColors(colors);
        
        graph.setGeometry(graphGeometry.getGeometryArray());
        graph.setAppearance(createGraphAppearance());
        
		return graph;
	}

	private Appearance createGraphAppearance() {
        Appearance materialAppear = new Appearance();
        PolygonAttributes polyAttrib = new PolygonAttributes();
        polyAttrib.setCullFace(PolygonAttributes.CULL_NONE);
        materialAppear.setPolygonAttributes(polyAttrib);

        Material material = new Material();
        material.setDiffuseColor(0.5f, 0.5f, 0.5f);
        materialAppear.setMaterial(material);

        return materialAppear;
	}
	
	private DirectionalLight createLighting() {
        DirectionalLight light = new DirectionalLight();
        light.setInfluencingBounds(bounds);
        light.setDirection(lightDirection);
        light.setColor(lightColor);
        
        return light;
	}
	
	private AmbientLight createAmbientLighting() {
        AmbientLight light = new AmbientLight();
        light.setInfluencingBounds(bounds);
        return light;
	}
	
	public static void main(String[] args) {
		new CalcGraph3D(null);
	}
}
