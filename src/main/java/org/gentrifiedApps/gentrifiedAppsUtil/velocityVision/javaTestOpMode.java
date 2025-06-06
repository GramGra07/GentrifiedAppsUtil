package org.gentrifiedApps.gentrifiedAppsUtil.velocityVision;

import org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.pipelines.bow.BlackAndWhiteDotDetector;
import org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.pipelines.bow.DotColor;
import org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.pipelines.bow.DotDetectionBuilder;
import org.opencv.core.Rect;

public class javaTestOpMode {
    BlackAndWhiteDotDetector blackAndWhiteDotDetector = new BlackAndWhiteDotDetector(
            DotColor.BLACK,
            new DotDetectionBuilder(
                    new Rect(50, 50, 100, 75), 10.0, 100.0
            )
    );
}
