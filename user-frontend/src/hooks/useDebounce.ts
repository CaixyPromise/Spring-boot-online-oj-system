import {useCallback, useRef} from 'react';

type CallbackFunction = (...args: any[]) => void;

function useDebounce(callback: CallbackFunction, delay: number): CallbackFunction
{
    const timeoutRef = useRef<NodeJS.Timeout | null>(null);

    return useCallback((...args: any[]) =>
    {
        if (timeoutRef.current) {
            clearTimeout(timeoutRef.current);
        }
        timeoutRef.current = setTimeout(() =>
        {
            callback(...args);
        }, delay);
    }, [callback, delay]);
}

export default useDebounce;