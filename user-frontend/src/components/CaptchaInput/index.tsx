import React, {useState, useEffect, useImperativeHandle, useRef} from 'react';
import {Button} from "@/components/ui/button";
import {Input} from "@/components/ui/input";
import Spinner from "@/components/Spinner";

interface CaptchaInputProps
{
    /**
     * 倒计时的秒数
     */
    cooldown?: number;
    /**
     * 获取验证码的方法
     */
    onGetCaptcha: (value: string) => Promise<boolean | void>;
    /**
     * 渲染按钮的文字
     */
    captchaTextRender?: (timing: boolean, count: number) => React.ReactNode;
}

export type CaptFieldRef = {
    startTiming: () => void;
    endTiming: () => void;
};

const VerificationCodeSender = React.forwardRef<
    CaptFieldRef, CaptchaInputProps>(({
                                          cooldown = 60,
                                          onGetCaptcha,
                                          captchaTextRender = (timing, count) => (timing ? `${count} 秒后重新获取` : '获取验证码')
                                      }, ref) =>
    {
        const [value, setValue] = useState('');
        const [count, setCount] = useState<number>(cooldown);
        const [timing, setTiming] = useState<boolean>(false);
        const [loading, setLoading] = useState<boolean>(false);
        const inputRef = useRef<HTMLInputElement | null>(null);
        const intervalRef = useRef<NodeJS.Timeout | null>(null); // Use useRef for the interval

        const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) =>
        {
            setValue(e.target.value);
        };

        const handleGetCaptcha = async () =>
        {
            try {
                let resultFlag;
                setLoading(true);
                try {
                    resultFlag = await onGetCaptcha(value);
                }
                catch (error)
                {
                    console.error(error);
                }
                setLoading(false);
                console.log("result: ", resultFlag)
                if (typeof resultFlag === 'boolean')
                {
                    if (resultFlag)
                    {
                        setTiming(true);
                    }
                }
                else
                {
                    setTiming(true)
                }
            }
            catch (error) {
                setLoading(false);
                console.error(error);
            }
        };

        // Expose startTiming and endTiming via ref
        useImperativeHandle(ref, () => ({
            startTiming: () =>
            {
                if (!timing) setTiming(true);
            },
            endTiming: () =>
            {
                if (intervalRef.current) {
                    clearInterval(intervalRef.current); // Clear the interval
                    intervalRef.current = null;
                }
                setTiming(false);
                setCount(cooldown);  // Reset countdown
            }
        }));

        useEffect(() =>
        {
            if (timing) {
                intervalRef.current = setInterval(() =>
                {
                    setCount(prevCount =>
                    {
                        if (prevCount <= 1) {
                            clearInterval(intervalRef.current!);
                            intervalRef.current = null;
                            setTiming(false);
                            return cooldown;
                        }
                        return prevCount - 1;
                    });
                }, 1000);
            }
            return () =>
            {
                if (intervalRef.current) {
                    clearInterval(intervalRef.current);
                }
            };
        }, [timing, cooldown]);

        return (
            <div style={{display: 'flex', alignItems: 'center', width: "100%"}}>
                <Input
                    value={value}
                    onChange={handleInputChange}
                    placeholder="Enter your email"
                    ref={inputRef}
                    style={{flex: 1, marginRight: '8px'}}
                />
                <Spinner loading={loading}>
                    <Button
                        onClick={handleGetCaptcha}
                        disabled={timing || loading || !value}
                        style={{marginLeft: '8px'}}
                    >
                        {captchaTextRender(timing, count)}
                    </Button>
                </Spinner>
            </div>
        );
    }
);

export default VerificationCodeSender;