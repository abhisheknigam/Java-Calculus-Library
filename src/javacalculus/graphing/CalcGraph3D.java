/**
 * 
 */
package javacalculus.graphing;

import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.GraphicsConfigTemplate3D;
import javax.media.j3d.Group;
import javax.media.j3d.Locale;
import javax.media.j3d.Material;
import javax.media.j3d.PhysicalBody;
import javax.media.j3d.PhysicalEnvironment;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.media.j3d.ViewPlatform;
import javax.media.j3d.VirtualUniverse;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.geometry.GeometryInfo;

/**
 * @author Duyun Chen <A HREF="mailto:duchen@seas.upenn.edu">[duchen@seas.upenn.edu]</A>,
 * Seth Shannin <A HREF="mailto:sshannin@seas.upenn.edu">[sshannin@seas.upenn.edu]</A>
 *  
 *
 */
public class CalcGraph3D extends JApplet implements Runnable {
	
	private static final long serialVersionUID = 7066480322477204662L;
	
	private JFrame frame;
	
	private VirtualUniverse universe;
	
	private Canvas3D canvas;
	
	private ViewPlatform viewPlatform;
	
	private View view;
	
	private GraphicsConfiguration config;
	
	private Locale locale;
	
	private Shape3D graph;
	
	//private Shape3D axes;
	
	private BranchGroup rootGroup;
	
	private TransformGroup rotateGroup;
	
	private Vector3f lightDirection = new Vector3f(0.0f, 0.0f, -1.0f);
	private Color3f lightColor = new Color3f(0.5f, 0.5f, 0.5f); 
	private Color3f graphColor = new Color3f(0.5f, 0.5f, 0.2f);
	
    //a bounding sphere specifies a region a behavior is active
    //create a sphere centered at the origin with radius of 1.0f
    private BoundingSphere bounds = new BoundingSphere();
	private CalcPlotter3D plotter;
    private float[] points;
    private Color3f[] colors;
    private int[] stripCount = new int[1];;
    
    private float 
    xmin = -1.0f, xmax = 1.0f, ymin = -1.0f, ymax = 1.0f;
    
    private float resolution = 0.2f;
    	
	/**
	 * Constructor
	 */
	public CalcGraph3D(CalcPlotter3D plotter) {
    	this.plotter = plotter;
    	this.points = new float[6*(int)((xmax - xmin)*(ymax - ymin)/(resolution*resolution))+3];
    	this.stripCount[0] = (int)(((xmax - xmin)*(ymax - ymin)/(resolution*resolution)+1));
    	this.colors = new Color3f[3*((int)((xmax - xmin)*(ymax - ymin)/(resolution*resolution)))+1];
    	populatePoints();
		createUniverse();
		frame = createFrame();
		EventQueue.invokeLater(this);
	}
	
	public void run() {
		locale = new Locale(universe);
		locale.addBranchGraph(rootGroup);
		
		frame.getContentPane().removeAll();
		frame.getContentPane().add(canvas);
		
		frame.setVisible(true);
		view.startView();
		
		canvas.requestFocus();
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
				float z = (float) plotter.getZValue(x, y);
				points[++index] = x;
				points[++index] = y;
				points[++index] = z;
				System.out.println("Point: (" + x + "," + y + "," + z + ")");
				colors[++colorIndex] = gradient;
				y += resolution;
			}
			x += resolution;
		}
	}
	
    protected JFrame createFrame() {
        JFrame window;
        window = new JFrame("PLOT3D - " + plotter.getFunction());
        window.setSize(600, 600);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        return window;
    }
    
    protected void createUniverse() {
        universe = new VirtualUniverse();

        config = GraphicsEnvironment.getLocalGraphicsEnvironment()
                                 .getDefaultScreenDevice()
                                 .getBestConfiguration(
                                     new GraphicsConfigTemplate3D());

        view = new View();
        view.setPhysicalBody(new PhysicalBody());
        view.setPhysicalEnvironment(new PhysicalEnvironment());

        rootGroup = createSceneBranchGroup();

        canvas = new Canvas3D(config);
        view.addCanvas3D(canvas);
    }
	
	private BranchGroup createSceneBranchGroup() {
		BranchGroup branchGroup = new BranchGroup();
		branchGroup.setCapability(BranchGroup.ALLOW_DETACH);
		
		TransformGroup rotateGroup = createGraphGroup();
		rotateGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		rotateGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		MouseRotate mouseRotator = new MouseRotate();
		mouseRotator.setTransformGroup(rotateGroup);
		mouseRotator.setSchedulingBounds(bounds);
		
		MouseZoom mouseZoomer = new MouseZoom();
		mouseZoomer.setTransformGroup(rotateGroup);
		mouseZoomer.setSchedulingBounds(bounds);		
		
		createEnvironment(branchGroup);
		
		rotateGroup.addChild(createLighting());
		
		rotateGroup.addChild(createAmbientLighting());
		
		branchGroup.addChild(createView());
		
		branchGroup.addChild(mouseRotator);
		
		branchGroup.addChild(mouseZoomer);
		
		branchGroup.addChild(rotateGroup);
		
		branchGroup.compile();
		
		return branchGroup;
	}
	
    private Group createView() {
        Transform3D viewTransform = new Transform3D();
        TransformGroup viewGroup = new TransformGroup(viewTransform);

        viewPlatform = new ViewPlatform();
        view.attachViewPlatform(viewPlatform);

        viewTransform.set(new Vector3f(.0f, 0f, 3.0f));
        viewGroup.setTransform(viewTransform);
        viewGroup.addChild(viewPlatform);

        return viewGroup;
    }
	
    protected void createEnvironment(BranchGroup rootGroup) {
    	BoundingSphere bounds = new BoundingSphere(new Point3d(), 2);
		AmbientLight ambient = new AmbientLight();
		DirectionalLight directional = new DirectionalLight();
		Background bg = new Background(1f, 1f, 1f);
		
		bg.setApplicationBounds(bounds);
		
		ambient.setInfluencingBounds(bounds);
		directional.setInfluencingBounds(bounds);
		directional.setDirection(-01f, -01.f, -01.f);
		//setSchedulingBounds(bounds);
		
		rootGroup.addChild(directional);
		rootGroup.addChild(ambient);
		rootGroup.addChild(bg);
    }
    
	private TransformGroup createGraphGroup() {
		rotateGroup = new TransformGroup();
		
        rotateGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        rotateGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        rotateGroup.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
        
		graph = new Shape3D();
		
		graph.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
		
		rotateGroup.addChild(graph);
		
		
		
		GeometryInfo graphGeometry = new GeometryInfo(GeometryInfo.POLYGON_ARRAY);
		graphGeometry.setCoordinates(points);
		graphGeometry.setStripCounts(stripCount);
//		graphGeometry.setColors(colors);
        
        graph.setGeometry(graphGeometry.getGeometryArray());
        graph.setAppearance(createGraphAppearance());
        
		return rotateGroup;
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
