package studio.archetype.firefight.ordnance.client.motiontracker;

import lombok.Data;
import studio.archetype.firefight.ordnance.client.motiontracker.enums.VerticalDiff;

@Data
public class RadarSegment {

    private boolean inner = false, outer = false;
    private VerticalDiff yDiff;

    public void reset() {
        inner = outer = false;
        yDiff =  null;
    }
}
