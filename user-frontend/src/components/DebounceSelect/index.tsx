import React, { useMemo, useRef, useState } from 'react';
import debounce from 'lodash/debounce';
import Spinner from "@/components/Spinner";
import { Command, CommandInput, CommandList, CommandItem, CommandEmpty, CommandGroup } from '@/components/ui/command';
import { Popover, PopoverTrigger, PopoverContent } from "@/components/ui/popover";
import { CaretSortIcon, CheckIcon } from "@radix-ui/react-icons";
import { cn } from "@/lib/utils";

export interface DebounceSelectProps<ValueType = any> {
    fetchOptions: (search: string) => Promise<ValueType[]>;
    debounceTimeout?: number;
    placeholder?: string;
}

function DebounceSelect<ValueType extends { key?: string; label: React.ReactNode; value: string | number } = any>({
  fetchOptions,
  debounceTimeout = 800,
  placeholder = "Select users",
  ...props
}: DebounceSelectProps<ValueType>) {
    const [fetching, setFetching] = useState(false);
    const [options, setOptions] = useState<ValueType[]>([]);
    const [open, setOpen] = useState(false);
    const [inputValue, setInputValue] = useState("");
    const fetchRef = useRef(0);

    const debounceFetcher = useMemo(() => {
        const loadOptions = (value: string) => {
            fetchRef.current += 1;
            const fetchId = fetchRef.current;
            setOptions([]);
            setFetching(true);

            fetchOptions(value).then(newOptions => {
                if (fetchId !== fetchRef.current) {
                    return;
                }
                setOptions(newOptions);
                setFetching(false);
            });
        };

        return debounce(loadOptions, debounceTimeout);
    }, [fetchOptions, debounceTimeout]);

    const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setInputValue(event.target.value);
        debounceFetcher(event.target.value);
    };

    return (
        <Popover open={open} onOpenChange={setOpen}>
            <PopoverTrigger asChild>
                <div
                    className="w-[200px] flex justify-between items-center cursor-pointer border p-2 rounded"
                    onClick={() => setOpen(!open)}
                >
                    {inputValue || placeholder}
                    <CaretSortIcon className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                </div>
            </PopoverTrigger>
            <PopoverContent className="w-[200px] p-0">
                <Spinner loading={fetching}>
                    <Command>
                        <CommandInput
                            value={inputValue}
                            onChange={handleInputChange}
                            placeholder={placeholder}
                            className="h-9"
                        />
                        <CommandList>
                            {options.length > 0 ? (
                                <CommandGroup>
                                    {options.map((option) => (
                                        <CommandItem
                                            key={option.value}
                                            onSelect={() => {
                                                setInputValue(option.label);
                                                setOpen(false);
                                            }}
                                        >
                                            {option.label}
                                            <CheckIcon
                                                className={cn("ml-auto h-4 w-4", inputValue === option.value ? "opacity-100" : "opacity-0")}
                                            />
                                        </CommandItem>
                                    ))}
                                </CommandGroup>
                            ) : (
                                <CommandEmpty>No results found.</CommandEmpty>
                            )}
                        </CommandList>
                    </Command>
                </Spinner>
            </PopoverContent>
        </Popover>
    );
}

export default DebounceSelect;
