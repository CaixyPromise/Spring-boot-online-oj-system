import * as React from "react"

import { cn } from "@/lib/utils"
import {Icons} from "@/components/ui/icons";
import {useState} from "react";

export interface InputProps
  extends React.InputHTMLAttributes<HTMLInputElement> {}

const Input = React.forwardRef<HTMLInputElement, InputProps>(
  ({ className, type, ...props }, ref) => {
    return (
      <input
        type={type}
        className={cn(
          "flex h-9 w-full rounded-md border border-input bg-transparent px-3 py-1 text-sm shadow-sm transition-colors file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-ring disabled:cursor-not-allowed disabled:opacity-50",
          className
        )}
        ref={ref}
        {...props}
      />
    )
  }
)
Input.displayName = "Input"

interface CustomInputComponent extends React.ForwardRefExoticComponent<InputProps & React.RefAttributes<HTMLInputElement>> {
    Password: typeof PasswordInput;
}
const EnhancedInput = Input as CustomInputComponent;

const PasswordInput = React.forwardRef<HTMLInputElement, InputProps>(
    (props, ref) => {
        const [showPassword, setShowPassword] = useState(false);

        const togglePasswordVisibility = () => {
            setShowPassword(!showPassword);
        };

        return (
            <div className="relative flex items-center">
                <Input
                    {...props}
                    ref={ref}
                    type={showPassword ? 'text' : 'password'}
                    className="pr-10" // 增加padding以避免图标覆盖文字
                />
                <span className="absolute inset-y-0 right-0 flex items-center pr-3">
          <span className="absolute inset-y-0 right-0 flex items-center pr-3 cursor-pointer">
                    {showPassword ? (
                        <Icons.eyeOpen
                            onClick={togglePasswordVisibility}
                            className="hover:text-gray-600 active:text-gray-1000"
                        />
                    ) : (
                        <Icons.eyeClose
                            onClick={togglePasswordVisibility}
                            className="hover:text-gray-600 active:text-gray-1000 active:border-gray-1000"
                        />
                    )}
                </span>
        </span>
            </div>
        );
    }
);

PasswordInput.displayName = 'PasswordInput';

EnhancedInput.Password = PasswordInput;

export { EnhancedInput as Input };

// export { Input }
