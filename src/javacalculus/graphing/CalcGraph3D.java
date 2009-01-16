/**
 * 
 */
package javacalculus.graphing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GraphicsConfiguration;

import javax.media.j3d.Alpha;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Material;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.ColorCube;
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
	
	private JFrame frame = new JFrame("PLOT3D");
	
	private Vector3f lightDirection = new Vector3f(0.0f, 0.0f, -1.0f);
	private Color3f lightColor = new Color3f(1.0f, 0.0f, 0.0f); 
	
    //a bounding sphere specifies a region a behavior is active
    //create a sphere centered at the origin with radius of 1.0f
    private BoundingSphere bounds = new BoundingSphere();
	
	/**
	 * Constructor
	 */
	public CalcGraph3D() {
		EventQueue.invokeLater(this);
	}
	
	public void run() {
        setPreferredSize(new Dimension(400, 400));
        setLayout(new BorderLayout());
        
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();

        Canvas3D canvas3D = new Canvas3D(config);

        BranchGroup scene = createSceneBranchGroup2();
        scene.compile();
        
        //Probably use a more sophisticated universe in the future
        SimpleUniverse simpleU = new SimpleUniverse(canvas3D);

		//This will move the ViewPlatform back a bit so the
		//objects in the scene can be viewed.
        simpleU.getViewingPlatform().setNominalViewingTransform();

        simpleU.addBranchGraph(scene);
        
        add("Center", canvas3D);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(this);
        frame.pack();
        frame.setVisible(true);
	}
	
	private BranchGroup createSceneBranchGroup2() {
		BranchGroup rootGroup = new BranchGroup();

		TransformGroup animateGroup = new TransformGroup();
		animateGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		Alpha alpha = new Alpha(-1, 1000);
		RotationInterpolator rInterpolator = new RotationInterpolator(alpha, animateGroup);
		rInterpolator.setSchedulingBounds(new BoundingSphere());
		animateGroup.addChild(rInterpolator);
		animateGroup.addChild(new ColorCube(0.5));
		
		Transform3D rotate = new Transform3D();
		rotate.rotX(Math.PI/4.0D);
		TransformGroup rotateGroup = new TransformGroup(rotate);

		rotateGroup.addChild(animateGroup);
		
		rootGroup.addChild(rotateGroup);
		
		return rootGroup;
	}
	
	private BranchGroup createSceneBranchGroup() {
		BranchGroup objRoot = new BranchGroup();
		
        Transform3D t3D = new Transform3D();
        t3D.setTranslation(new Vector3f(0.0f, 0.0f, -2.0f));
        TransformGroup objMove = new TransformGroup(t3D);
        objRoot.addChild(objMove);
        
        TransformGroup objSpin = new TransformGroup();
        objSpin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objSpin.addChild(createGraph());

//        // Create the transform group node and initialize it to the 
//        // identity. Add it to the root of the subgraph.
//        TransformGroup objSpin = new TransformGroup();
//        objSpin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
//        objMove.addChild(objSpin);
//
//        // Create a simple shape leaf node, add it to the scene graph.
//        Font3D font3D = new Font3D(new Font("Helvetica", Font.PLAIN, 1),
//                                   new FontExtrusion());
//        Text3D textGeom = new Text3D(font3D, new String("3DText"));
//        textGeom.setAlignment(Text3D.ALIGN_CENTER);
//        Shape3D textShape = new Shape3D();
//        textShape.setGeometry(textGeom);
//        textShape.setAppearance(createGraphAppearance());
//        objSpin.addChild(textShape);
//
        // Create a new Behavior object that will perform the desired
        // operation on the specified transform object and add it into
        // the scene graph.
        Alpha rotationAlpha = new Alpha(-1, 10000);
 
        RotationInterpolator rotator =
                new RotationInterpolator(rotationAlpha, objSpin);
        
        rotator.setSchedulingBounds(bounds);
        objSpin.addChild(rotator);

        //directional lighting
        objRoot.addChild(createLighting());

        //ambient lighting
        objRoot.addChild(createAmbientLighting());
        
        objRoot.addChild(objSpin);
        
		return objRoot;
	}
	
	private Shape3D createGraph() {
		Shape3D graph = new Shape3D();
		
		float[] vertices = {0.0f, 0.1f, 0.2f, 0.0f, 0.2f, 0.2f, -0.1f, 0.1f, 0.1f};
		int[] stripCounts = {3};
		
		GeometryInfo graphGeometry = new GeometryInfo(GeometryInfo.POLYGON_ARRAY);
		graphGeometry.setCoordinates(vertices);
		graphGeometry.setStripCounts(stripCounts);
		
		Triangulator triangulator = new Triangulator();
		triangulator.triangulate(graphGeometry);
		graphGeometry.recomputeIndices();
		
        Stripifier stripifier = new Stripifier();
        stripifier.stripify(graphGeometry);
        graphGeometry.recomputeIndices();
        
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
        material.setDiffuseColor(1.0f, 0.0f, 0.0f);
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
		new CalcGraph3D();
	}
}
