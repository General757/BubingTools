package com.bubing.tools.loading.render;

import android.content.Context;
import android.util.SparseArray;

import com.bubing.tools.loading.render.animal.FishLoadingRenderer;
import com.bubing.tools.loading.render.animal.GhostsEyeLoadingRenderer;
import com.bubing.tools.loading.render.circle.jump.CollisionLoadingRenderer;
import com.bubing.tools.loading.render.circle.jump.DanceLoadingRenderer;
import com.bubing.tools.loading.render.circle.jump.GuardLoadingRenderer;
import com.bubing.tools.loading.render.circle.jump.SwapLoadingRenderer;
import com.bubing.tools.loading.render.circle.rotate.GearLoadingRenderer;
import com.bubing.tools.loading.render.circle.rotate.LevelLoadingRenderer;
import com.bubing.tools.loading.render.circle.rotate.LinesLoadingRenderer;
import com.bubing.tools.loading.render.circle.rotate.LoopLoadingRenderer;
import com.bubing.tools.loading.render.circle.rotate.MaterialLoadingRenderer;
import com.bubing.tools.loading.render.circle.rotate.WhiteLoadingRenderer;
import com.bubing.tools.loading.render.circle.rotate.WhorlLoadingRenderer;
import com.bubing.tools.loading.render.goods.BalloonLoadingRenderer;
import com.bubing.tools.loading.render.goods.WaterBottleLoadingRenderer;
import com.bubing.tools.loading.render.scenery.DayNightLoadingRenderer;
import com.bubing.tools.loading.render.scenery.ElectricFanLoadingRenderer;
import com.bubing.tools.loading.render.shapechange.CircleBroodLoadingRenderer;
import com.bubing.tools.loading.render.shapechange.CoolWaitLoadingRenderer;

import java.lang.reflect.Constructor;

/**
 * @ClassName: LoadingRendererFactory
 * @Author: Bubing
 * @Date: 2020/8/24 3:40 PM
 * @Description: java类作用描述
 */
public final class LoadingRendererFactory {
    private static final SparseArray<Class<? extends LoadingRenderer>> LOADING_RENDERERS = new SparseArray<>();

    static {
        //circle rotate
        LOADING_RENDERERS.put(0, MaterialLoadingRenderer.class);
        LOADING_RENDERERS.put(1, LevelLoadingRenderer.class);
        LOADING_RENDERERS.put(2, WhorlLoadingRenderer.class);
        LOADING_RENDERERS.put(3, GearLoadingRenderer.class);
        //circle jump
        LOADING_RENDERERS.put(4, SwapLoadingRenderer.class);
        LOADING_RENDERERS.put(5, GuardLoadingRenderer.class);
        LOADING_RENDERERS.put(6, DanceLoadingRenderer.class);
        LOADING_RENDERERS.put(7, CollisionLoadingRenderer.class);
        //scenery
        LOADING_RENDERERS.put(8, DayNightLoadingRenderer.class);
        LOADING_RENDERERS.put(9, ElectricFanLoadingRenderer.class);
        //animal
        LOADING_RENDERERS.put(10, FishLoadingRenderer.class);
        LOADING_RENDERERS.put(11, GhostsEyeLoadingRenderer.class);
        //goods
        LOADING_RENDERERS.put(12, BalloonLoadingRenderer.class);
        LOADING_RENDERERS.put(13, WaterBottleLoadingRenderer.class);
        //shape change
        LOADING_RENDERERS.put(14, CircleBroodLoadingRenderer.class);
        LOADING_RENDERERS.put(15, CoolWaitLoadingRenderer.class);

        LOADING_RENDERERS.put(16, LoopLoadingRenderer.class);
        LOADING_RENDERERS.put(17, WhiteLoadingRenderer.class);
        LOADING_RENDERERS.put(18, LinesLoadingRenderer.class);
    }

    private LoadingRendererFactory() {
    }

    public static LoadingRenderer createLoadingRenderer(Context context, int loadingRendererId) throws Exception {
        Class<?> loadingRendererClazz = LOADING_RENDERERS.get(loadingRendererId);
        Constructor<?>[] constructors = loadingRendererClazz.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            if (parameterTypes != null && parameterTypes.length == 1 && parameterTypes[0].equals(Context.class)) {
                constructor.setAccessible(true);
                return (LoadingRenderer) constructor.newInstance(context);
            }
        }

        throw new InstantiationException();
    }
}
