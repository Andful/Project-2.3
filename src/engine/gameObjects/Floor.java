package engine.gameObjects;

import javax.media.j3d.IndexedQuadArray;
import javax.media.j3d.Node;
import javax.media.j3d.Shape3D;
import javax.vecmath.Point3f;
import javax.vecmath.TexCoord2f;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import java.awt.*;

/**
 * Created by Andrea Nardi on 6/13/2017.
 */
public class Floor extends Shape3D
{
    public Floor(Vector2f size,Component component)
    {
        super(getVertices(size),Agent.getAppearance(component,"res//obstacle.jpg"));
    }
    private static IndexedQuadArray getVertices(Vector2f size)
    {
        IndexedQuadArray indexedCube = new IndexedQuadArray(8,
                IndexedQuadArray.COORDINATES | IndexedQuadArray.NORMALS
                        | IndexedQuadArray.TEXTURE_COORDINATE_2, 4);
        Point3f[] cubeCoordinates = { new Point3f(-0.5f+size.x, -0.5f, -0.5f+size.y),
                new Point3f(-0.5f, -0.5f, -0.5f+size.y),
                new Point3f(-0.5f+size.x, -0.5f, -0.5f),
                new Point3f(-0.5f, -0.5f, -0.5f)};
        Vector3f[] normals = { new Vector3f(0.0f, 1f, 0f)};
        //Define the texture coordinates. These are defined
        //as floating point pairs of values that are used to
        //map the corners of the texture image onto the vertices
        //of the face. We then define the indices into this
        //array of values in a similar way to that used for
        //the vertices and normals.
        TexCoord2f[] textCoord = { new TexCoord2f(size.x, size.y),
                new TexCoord2f(0.0f, size.y), new TexCoord2f(0.0f, 0.0f),
                new TexCoord2f(size.x, 0.0f) };
        int coordIndices[] = { 0, 2,3,1};
        int normalIndices[] = { 0, 0, 0, 0};
        int textIndices[] = { 0, 3,2,1};
        indexedCube.setCoordinates(0, cubeCoordinates);
        indexedCube.setCoordinateIndices(0, coordIndices);
        indexedCube.setNormals(0, normals);
        indexedCube.setNormalIndices(0, normalIndices);
        indexedCube.setTextureCoordinates(0, 0, textCoord);
        indexedCube.setTextureCoordinateIndices(0, 0, textIndices);
        return indexedCube;
    }
}
