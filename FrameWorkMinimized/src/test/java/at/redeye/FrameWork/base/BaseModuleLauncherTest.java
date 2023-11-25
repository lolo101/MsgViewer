package at.redeye.FrameWork.base;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BaseModuleLauncherTest {

    @Nested
    class GivenNoArg {
        private final BaseModuleLauncher sut = new BaseModuleLauncher();

        @ParameterizedTest
        @ValueSource(strings = {"No way !", "false"})
        void should_enable_Splash(String propertyValue) {
            System.setProperty("NOSPLASH", propertyValue);
            assertTrue(sut.splashEnabled());
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "ja",
                "yes",
                "true",
                "1",
                "x",
                "+"
        })
        void should_disable_Splash(String propertyValue) {
            System.setProperty("NOSPLASH", propertyValue);
            assertFalse(sut.splashEnabled());
        }
    }

    @Nested
    class GivenNoSplashArg {
        private final BaseModuleLauncher sut = new BaseModuleLauncher("-nosplash");

        @Test
        void should_disable_Splash() {
            assertFalse(sut.splashEnabled());
        }
    }
}