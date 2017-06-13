package engine;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.image.TextureLoader;

import javax.imageio.ImageIO;
import javax.media.j3d.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.TexCoord2f;
import javax.vecmath.Vector3f;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Andrea Nardi on 6/12/2017.
 */
public class Agent extends TransformGroup
{
    public static Shape3D red;
    public static Shape3D blue;
    public Node thisRed;
    public Node thisBlue;
    public Vector3f position;
    public Agent()
    {
        thisRed=red.cloneNode(true);
        thisBlue=blue.cloneNode(true);
        setCapability(ALLOW_TRANSFORM_WRITE);
        addChild(thisBlue);
    }
    public void update()
    {
        setTransform(new Transform3D(){{setTranslation(position);}});
    }
    public void moved(boolean moved)
    {
        removeAllChildren();
        if(moved)
        {
            addChild(thisRed);
        }
        else
        {
            addChild(thisBlue);
        }
    }
    public static void loadMesh(Component component)
    {
        red=cubeMesh(component,"res\\red.jpg");
        blue=cubeMesh(component,"res\\blue.jpg");
    }
    public static Shape3D cubeMesh(Component component,String fileName)
    {
        IndexedQuadArray indexedCube = new IndexedQuadArray(8,
                IndexedQuadArray.COORDINATES | IndexedQuadArray.NORMALS
                        | IndexedQuadArray.TEXTURE_COORDINATE_2, 24);
        Point3f[] cubeCoordinates = { new Point3f(0.5f, 0.5f, 0.5f),
                new Point3f(-0.5f, 0.5f, 0.5f),
                new Point3f(-0.5f, -0.5f, 0.5f),
                new Point3f(0.5f, -0.5f, 0.5f), new Point3f(0.5f, 0.5f, -0.5f),
                new Point3f(-0.5f, 0.5f, -0.5f),
                new Point3f(-0.5f, -0.5f, -0.5f),
                new Point3f(0.5f, -0.5f, -0.5f) };
        Vector3f[] normals = { new Vector3f(0.0f, 0.0f, 1f),
                new Vector3f(0.0f, 0.0f, -1f),
                new Vector3f(1f, 0.0f, 0.0f),
                new Vector3f(-1f, 0.0f, 0.0f),
                new Vector3f(0.0f, 1f, 0.0f), new Vector3f(0.0f, -1f, 0.0f) };
        //Define the texture coordinates. These are defined
        //as floating point pairs of values that are used to
        //map the corners of the texture image onto the vertices
        //of the face. We then define the indices into this
        //array of values in a similar way to that used for
        //the vertices and normals.
        TexCoord2f[] textCoord = { new TexCoord2f(1f, 1f),
                new TexCoord2f(0.0f, 1f), new TexCoord2f(0.0f, 0.0f),
                new TexCoord2f(1f, 0.0f) };
        int coordIndices[] = { 0, 1, 2, 3, 7, 6, 5, 4, 0, 3, 7, 4, 5, 6, 2, 1,
                0, 4, 5, 1, 6, 7, 3, 2 };
        int normalIndices[] = { 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3,
                4, 4, 4, 4, 5, 5, 5, 5 };
        int textIndices[] = { 0, 1, 2, 3, 3, 0, 1, 2, 1, 2, 3, 0, 1, 2, 3, 0,
                3, 0, 1, 2, 1, 2, 3, 0 };
        indexedCube.setCoordinates(0, cubeCoordinates);
        indexedCube.setCoordinateIndices(0, coordIndices);
        indexedCube.setNormals(0, normals);
        indexedCube.setNormalIndices(0, normalIndices);
        indexedCube.setTextureCoordinates(0, 0, textCoord);
        indexedCube.setTextureCoordinateIndices(0, 0, textIndices);
        return new Shape3D(indexedCube,getAppearance(component,fileName));
    }
    public static Appearance getAppearance(Component component,String fileName)
    {
        //Load the texture from the external image file
        TextureLoader textLoad = new TextureLoader(fileName, component);
        //Access the image from the loaded texture
        ImageComponent2D textImage = textLoad.getImage();
        //Create a two dimensional texture
        Texture2D texture = new Texture2D(Texture2D.BASE_LEVEL, Texture.RGB,
                textImage.getWidth(), textImage.getHeight());
        //Set the texture from the image loaded
        texture.setImage(0, textImage);
        //Create the appearance that will use the texture
        Appearance app = new Appearance();
        app.setTexture(texture);
        //Define how the texture will be mapped onto the surface
        //by creating the appropriate texture attributes
        TextureAttributes textAttr = new TextureAttributes();
        textAttr.setTextureMode(TextureAttributes.REPLACE);
        app.setTextureAttributes(textAttr);
        app.setMaterial(new Material());
        return app;
    }
}
