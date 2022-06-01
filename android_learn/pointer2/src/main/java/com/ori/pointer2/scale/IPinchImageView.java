package com.ori.pointer2.scale;

import android.graphics.Matrix;
import android.graphics.RectF;

/**
 * @author：zhuwentao
 * @time: 2021-07-26
 * @describe:setIm
 */
public interface IPinchImageView {

    /**
     * 设置单击事件
     */
    void setOnSingleClickListener(PinchImageView.OnPinchSingleClickListener listener);

    /**
     * 设置长按事件
     */
    void setOnLongClickListener(PinchImageView.OnPinchLongClickListener listener);

    /**
     * 设置双击事件
     *
     * @param listener
     */
    void setOnDoubleClickListener(PinchImageView.OnPinchDoubleClickListener listener);

    /**
     * 获取外部变换矩阵.
     * <p>
     * 外部变换矩阵记录了图片手势操作的最终结果,是相对于图片fit center状态的变换.
     * 默认值为单位矩阵,此时图片为fit center状态.
     *
     * @param matrix 用于填充结果的对象
     * @return 如果传了matrix参数则将matrix填充后返回, 否则new一个填充返回
     */
    Matrix getOuterMatrix(Matrix matrix);

    /**
     * 获取内部变换矩阵.
     * <p>
     * 内部变换矩阵是原图到fit center状态的变换,当原图尺寸变化或者控件大小变化都会发生改变
     * 当尚未布局或者原图不存在时,其值无意义.所以在调用前需要确保前置条件有效,否则将影响计算结果.
     *
     * @param matrix 用于填充结果的对象
     * @return 如果传了matrix参数则将matrix填充后返回, 否则new一个填充返回
     */
    Matrix getInnerMatrix(Matrix matrix);

    /**
     * 获取图片总变换矩阵.
     * <p>
     * 总变换矩阵为内部变换矩阵x外部变换矩阵,决定了原图到所见最终状态的变换
     * 当尚未布局或者原图不存在时,其值无意义.所以在调用前需要确保前置条件有效,否则将影响计算结果.
     *
     * @param matrix 用于填充结果的对象
     * @return 如果传了matrix参数则将matrix填充后返回, 否则new一个填充返回
     * @see #getOuterMatrix(Matrix)
     * @see #getInnerMatrix(Matrix)
     */
    Matrix getCurrentImageMatrix(Matrix matrix);

    /**
     * 获取当前变换后的图片位置和尺寸
     * <p>
     * 当尚未布局或者原图不存在时,其值无意义.所以在调用前需要确保前置条件有效,否则将影响计算结果.
     *
     * @param rectF 用于填充结果的对象
     * @return 如果传了rectF参数则将rectF填充后返回, 否则new一个填充返回
     * @see #getCurrentImageMatrix(Matrix)
     */
    RectF getImageBound(RectF rectF);

    /**
     * 获取当前设置的mask
     *
     * @return 返回当前的mask对象副本, 如果当前没有设置mask则返回null
     */
    RectF getMask();

    /**
     * 获取当前手势状态
     * PINCH_MODE_FREE
     * PINCH_MODE_SCROLL
     * PINCH_MODE_SCALE
     */
    int getPinchMode();

    /**
     * 设置PinchMode监听
     */
    void setPinchModePerformanceListener(PinchImageView.PinchModePerformanceListener listener);

    /**
     * 执行当前outerMatrix到指定outerMatrix渐变的动画
     * <p>
     * 调用此方法会停止正在进行中的手势以及手势动画.
     * 当duration为0时,outerMatrix值会被立即设置而不会启动动画.
     *
     * @param endMatrix 动画目标矩阵
     * @param duration  动画持续时间
     * @see #getOuterMatrix(Matrix)
     */
    void outerMatrixTo(Matrix endMatrix, long duration);


    /**
     * 执行当前mask到指定mask的变化动画
     * <p>
     * 调用此方法不会停止手势以及手势相关动画,但会停止正在进行的mask动画.
     * 当前mask为null时,则不执行动画立即设置为目标mask.
     * 当duration为0时,立即将当前mask设置为目标mask,不会执行动画.
     *
     * @param mask     动画目标mask
     * @param duration 动画持续时间
     * @see #getMask()
     */
    void zoomMaskTo(RectF mask, long duration);

    /**
     * 重置所有状态
     * <p>
     * 重置位置到fit center状态,清空mask,停止所有手势,停止所有动画.
     * 但不清空drawable,以及事件绑定相关数据.
     */
    void reset();

    /**
     * 添加外部矩阵变化监听
     */
    void addOuterMatrixChangedListener(PinchImageView.OuterMatrixChangedListener listener);

    /**
     * 删除外部矩阵变化监听
     */
    void removeOuterMatrixChangedListener(PinchImageView.OuterMatrixChangedListener listener);

    /**
     * 设置题卡的拖拽、缩放限制区域
     *
     * @param rectF
     */
    void setLimitRectF(RectF rectF);
}
