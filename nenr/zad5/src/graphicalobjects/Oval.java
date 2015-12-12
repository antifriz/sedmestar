package graphicalobjects;

import geometry.Point;
import geometry.Rectangle;
import rendering.Renderer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by ivan on 6/16/15.
 */
public class Oval extends AbstractGraphicalObject {
    public Oval(Point[] hotPoints) {
        super(hotPoints);
    }

    public Oval() {
        this(new Point[]{new Point(0, 10), new Point(10, 0)});
    }

    @Override
    public String getShapeName() {
        return "Oval";
    }

    @Override
    public void render(Renderer r) {
        Point center = new Point(hotPoints[0].getX(), hotPoints[1].getY());
        Point axis = new Point(hotPoints[1].getX() - hotPoints[0].getX(), hotPoints[1].getY() - hotPoints[0].getY());

        List<Point> points = new ArrayList<>();
        double angle = 0;
        double step = 2 * Math.PI / 64;
        while (angle < 2 * Math.PI) {
            angle += step;
            Point p = center.translate(new Point((int) Math.round(axis.getX() * Math.cos(angle)), (int) Math.round(axis.getY() * Math.sin(angle))));
            points.add(p);
        }
        Point[] pointsArr = points.toArray(new Point[64]);
        r.fillPolygon(pointsArr);
    }

    @Override
    public GraphicalObject duplicate() {
        return new Oval();
    }

    @Override
    public String getShapeID() {
        return "@OVAL";
    }

    @Override
    public void load(Stack<GraphicalObject> stack, String data) {
        String[] parts = data.split(" ");
        stack.push(new Oval(new Point[]{new Point(Integer.parseInt(parts[0]),Integer.parseInt(parts[1])),new Point(Integer.parseInt(parts[2]),Integer.parseInt(parts[3]))}));
    }

    @Override
    public void save(List<String> rows) {
        rows.add(String.format("%s %d %d %d %d",getShapeID(),hotPoints[0].getX(),hotPoints[0].getY(),hotPoints[1].getX(),hotPoints[1].getY()));
    }

    @Override
    public Rectangle getBoundingBox() {
        Point center = new Point(hotPoints[0].getX(), hotPoints[1].getY());
        Point axis = new Point(hotPoints[1].getX() - hotPoints[0].getX(), hotPoints[0].getY() - hotPoints[1].getY());

        return new Rectangle(center.getX()-axis.getX(),center.getY()-axis.getY(),2*axis.getX(),2*axis.getY());
   }
}
