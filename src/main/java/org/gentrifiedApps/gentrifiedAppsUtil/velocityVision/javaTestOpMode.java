package org.gentrifiedApps.gentrifiedAppsUtil.velocityVision;

import org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.classes.CameraParams;
import org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.classes.DefaultLensIntrinsics;
import org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.classes.RotationVector;
import org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.classes.TranslationalVector;
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
