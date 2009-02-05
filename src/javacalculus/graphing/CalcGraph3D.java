/**
 * 
 */
package javacalculus.graphing;

import java.awt.EventQueue;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.GraphicsConfigTemplate3D;
import javax.media.j3d.Group;
import javax.media.j3d.LineArray;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.Locale;
import javax.media.j3d.Material;
import javax.media.j3d.PhysicalBody;
import javax.media.j3d.PhysicalEnvironment;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TriangleArray;
import javax.media.j3d.View;
import javax.media.j3d.ViewPlatform;
import javax.media.j3d.VirtualUniverse;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;

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
	private Shape3D axes;
	
	private BranchGroup rootGroup;
	private TransformGroup rotateGroup;
	
	private Vector3f lightDirection = new Vector3f(0.0f, 0.0f, -1.0f);
	private Color3f lightColor = new Color3f(0.5f, 0.5f, 0.5f); 
	
    //a bounding sphere specifies a region a behavior is active
    //create a sphere centered at the origin with radius of 1.0f
    private BoundingSphere bounds = new BoundingSphere();
	private CalcPlotter3D plotter;
    private double[][] points;
    
    private double
    x_min = -10, x_max = 10, y_min = -10, y_max = 10, z_max = 10, z_min = -10;
    
    private int resolution = 120;
    	
	/**
	 * Constructor
	 */
	public CalcGraph3D(CalcPlotter3D plotter) {
    	this.plotter = plotter;
    	this.points = new double[resolution+1][resolution+1];
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
		for (int x = 0; x <= resolution; ++x) {
			for (int y = 0; y <= resolution; ++y) {
				try {
					points[x][y] = plotter.getZValue(	x_min + (x * (x_max - x_min))/resolution, 
														y_min + (y * (y_max - y_min))/resolution);
                    if (points[x][y] > z_max)
                        z_max = points[x][y];
                    if (points[x][y] < z_min)
                        z_min = points[x][y];
                } catch (Exception e) {
                    points[x][y] = Double.POSITIVE_INFINITY;
                }
            }
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
        graph.setGeometry(createGraphGeometry());
        graph.setAppearance(createGraphAppearance());
         
		createAxesGeometry();
		
		rotateGroup.addChild(graph);
		rotateGroup.addChild(axes);
		 
		return rotateGroup;
	}
	
	private void createAxesGeometry() {
        axes = new Shape3D();
        Appearance axesAppearance = new Appearance();
        axesAppearance.setLineAttributes(new LineAttributes(1f, LineAttributes.PATTERN_SOLID, true));
        axesAppearance.setColoringAttributes(new ColoringAttributes(new Color3f(), ColoringAttributes.NICEST));
        axes.setAppearance(axesAppearance);
        axes.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
        
        LineArray axesArray = new LineArray(6, LineArray.COORDINATES);

        axesArray.setCoordinate(0, new Point3d(-2.0d, 0.0d, 0.0d));	//x axis
        axesArray.setCoordinate(1, new Point3d(2.0d, 0.0d, 0.0d));
        axesArray.setCoordinate(2, new Point3d(0.0d, -2.0d, 0.0d));	//y axis
        axesArray.setCoordinate(3, new Point3d(0.0d, 2.0d, 0.0d));
        axesArray.setCoordinate(4, new Point3d(0.0d, 0.0d, -2.0d));	//z axis
        axesArray.setCoordinate(5, new Point3d(0.0d, 0.0d, 2.0d));
        axes.addGeometry(axesArray);
	}

	private GeometryArray createGraphGeometry() {
		TriangleArray arr = new TriangleArray(6*resolution*resolution,
				TriangleArray.COORDINATES | TriangleArray.NORMALS | TriangleArray.COLOR_3);
		Point3d p1 = new Point3d();
		Point3d p2 = new Point3d();
		Point3d p3 = new Point3d();
		Color3f c1 = new Color3f();
		Color3f c2 = new Color3f();
		Color3f c3 = new Color3f();
		Vector3d normal = new Vector3d();
		Vector3d v1 = new Vector3d();
		Vector3d v2 = new Vector3d();
		Vector3f normalF = new Vector3f();
		int triangle = 0;
		int x = 0;
		int y = 0;

		for (x = 0; x < resolution; ++x) {
			y = 0;
			p1.set(transform(x), transform(y), transform(x, y));
			c1.set(getColor(points[x][y]));
			p2.set(transform(x + 1), transform(y), transform(x + 1, y));
			c2.set(getColor(points[x + 1][y]));
			for (; y < resolution; ++y) {
				p3.set(transform(x), transform(y + 1), transform(x, y + 1));
				c3.set(getColor(points[x][y + 1]));
				createUpperTriangle(arr, p1, p2, p3, c1, c2, c3, normal, v1, v2, normalF, triangle, x, y);
				triangle += 3;
				createLowerTriangle(arr, p1, p2, p3, c1, c2, c3, normal, v1, v2, normalF, triangle, x, y);
				triangle += 3;
			}
		}
		return arr;
	}
	
	private void createUpperTriangle(TriangleArray arr, 			
									Point3d p1,
						            Point3d p2,
						            Point3d p3,
						            Color3f c1,
						            Color3f c2,
						            Color3f c3,
						            Vector3d normal,
						            Vector3d v1,
						            Vector3d v2,
						            Vector3f normalF,
						            int triangle,
						            int x,
						            int y) {
		if (inBounds(points[x][y], points[x + 1][y], points[x][y + 1])) {
			v1.sub(p2, p1);
			v2.sub(p3, p1);
			normal.cross(v1, v2);
			normalF.set(normal);
			normalF.normalize();
			arr.setNormal(triangle, normalF);
			arr.setNormal(triangle + 1, normalF);
			arr.setNormal(triangle + 2, normalF);

			arr.setCoordinate(triangle, p1);
			arr.setColor(triangle, c1);
			arr.setCoordinate(triangle + 1, p2);
			arr.setColor(triangle + 1, c2);
			arr.setCoordinate(triangle + 2, p3);
			arr.setColor(triangle + 2, c3);
		}
	}
	
    private void createLowerTriangle(TriangleArray arr,
						            Point3d p1,
						            Point3d p2,
						            Point3d p3,
						            Color3f c1,
						            Color3f c2,
						            Color3f c3,
						            Vector3d normal,
						            Vector3d v1,
						            Vector3d v2,
						            Vector3f normalF,
						            int triangle,
						            int x,
						            int y) {
    	p1.set(p3);
    	c1.set(c3);
    	p3.set(p2);
    	c3.set(c2);
    	p2.set(transform(x + 1), transform(y + 1), transform(x + 1, y + 1));
    	c2.set(getColor(points[x + 1][y + 1]));

    	if (inBounds(points[x + 1][y], points[x][y + 1], points[x + 1][y + 1])) {
    		v1.sub(p2, p1);
    		v2.sub(p3, p1);
    		normal.cross(v1, v2);
    		normalF.set(normal);
    		normalF.normalize();
    		arr.setNormal(triangle, normalF);
    		arr.setNormal(triangle + 1, normalF);
    		arr.setNormal(triangle + 2, normalF);

    		arr.setCoordinate(triangle, p1);
    		arr.setColor(triangle, c1);
    		arr.setCoordinate(triangle + 1, p2);
    		arr.setColor(triangle + 1, c2);
    		arr.setCoordinate(triangle + 2, p3);
    		arr.setColor(triangle + 2, c3);
    	}
    }
    
    private boolean inBounds(double a, double b, double c) {
    	return a <= z_max && a >= z_min && b <= z_max && b >= z_min && c <= z_max && c >= z_min;
    }
    
	private double transform(int value) {
        return (double)value / (double)resolution - 0.5d;
	}
	
	private double transform(int x, int y) {
		return (points[x][y] / (z_max - z_min));
	}
	
	private Color3f getColor(double value) {
		Color3f color = new Color3f();
        color.set(1f, 1f - (float)((value - z_min) / (z_max - z_min)), 0f);
        return color;
	}
	
	private Appearance createGraphAppearance() {
        Appearance materialAppear = new Appearance();
        materialAppear.setPolygonAttributes(new PolygonAttributes(
                PolygonAttributes.POLYGON_FILL,
                PolygonAttributes.CULL_NONE,
                0f, true));

        materialAppear.setMaterial(new Material(
                new Color3f(0.3f,
                            0.3f,
                            0.3f),
                new Color3f(),
                new Color3f(.6f, .6f ,.6f),
                new Color3f(1f, 1f, 1f), 64f));

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
