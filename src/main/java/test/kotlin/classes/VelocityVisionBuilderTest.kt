package test.kotlin.classes

import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.Alliance
import org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.pipelines.bow.BlackAndWhiteDotDetector
import org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.pipelines.bow.DotColor
import org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.pipelines.bow.DotDetectionBuilder
import org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.pipelines.moa.AssumedBuilder
import org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.pipelines.moa.DetectionBuilder
import org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.pipelines.moa.MeanColorOfAreaDetector
import org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.pipelines.sample.ReturnType
import org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.pipelines.sample.SampleDataDetector
import org.junit.Test
import org.opencv.core.Rect
import org.opencv.core.Scalar
import org.testng.Assert.assertThrows

class VelocityVisionBuilderTest {
    @Test
    fun testBOW() {
        assertThrows {
            BlackAndWhiteDotDetector(
                DotColor.BLACK,
                DotDetectionBuilder(
                    Rect(50, 50, 100, 75), 10.0, 100.0
                )
            )
        }
    }

    @Test
    fun testSampleDataDetector() {
        assertThrows {
            SampleDataDetector(
                ReturnType.Companion.all(),
                Alliance.RED
            )
        }
    }

    @Test
    fun testMOA() {
        assertThrows {
            MeanColorOfAreaDetector(
                DetectionBuilder(
                    Rect(50, 50, 100, 75),
                    "testd1",
                    Scalar(0.0, 0.0, 0.0),
                    Scalar(0.0, 0.0, 0.0)
                ),
                DetectionBuilder(
                    Rect(50, 50, 100, 75),
                    "testd2",
                    Scalar(0.0, 0.0, 0.0),
                    Scalar(0.0, 0.0, 0.0)
                ),
                AssumedBuilder { println("Assumed") }
            )
        }
    }
}