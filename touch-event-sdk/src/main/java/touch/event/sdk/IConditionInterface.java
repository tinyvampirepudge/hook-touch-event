package touch.event.sdk;

/**
 * @Description: 条件接口
 * @Author wangjianzhou
 * @Date 2022/6/21 16:03
 * @Version v0.9.5
 */
interface IConditionInterface<T,R> {
    R condition(T t);
}
