package model;

/**
 * A component that occupies a single point.
 * Has a width and height of 1
 */
abstract class DotComponent extends LevelComponent{
    /** Creates the component at a point
     * @param position Tne point that the component occupies
     */
    DotComponent(Coordinates position){
        super(1,1,position);
    }
}
