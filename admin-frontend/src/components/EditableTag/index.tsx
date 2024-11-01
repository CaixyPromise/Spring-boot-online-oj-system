import {Input, InputRef, Tag, theme} from "antd";
import React, {useEffect, useRef, useState} from "react";
import {TweenOneGroup} from "rc-tween-one";
import {PlusOutlined} from "@ant-design/icons";

interface EditableTagProps {
    tags: string[];
    setTags: React.Dispatch<React.SetStateAction<string[]>>;
}

const TagComponent: React.FC<EditableTagProps> = ({ tags, setTags }) =>
{
    const { token } = theme.useToken();
    const [ inputVisible, setInputVisible ] = useState<boolean>(false);
    const [ inputValue, setInputValue ] = useState<string>('');
    const inputRef = useRef<InputRef>(null);

    useEffect(() =>
    {
        if (inputVisible)
        {
            inputRef.current?.focus();
        }
    }, [ inputVisible ]);

    const handleClose = (removedTag: string) =>
    {
        const newTags = tags.filter((tag) => tag !== removedTag);
        setTags(newTags);
    };

    const showInput = () =>
    {
        setInputVisible(true);
    };

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) =>
    {
        setInputValue(e.target.value);
    };

    const handleInputConfirm = () =>
    {
        if (inputValue && tags.indexOf(inputValue) === -1)
        {
            setTags([ ...tags, inputValue ]);
        }
        setInputVisible(false);
        setInputValue('');
    };

    const forMap = (tag: string) =>
    {
        const tagElem = (
            <Tag
                closable
                onClose={(e) =>
                {
                    e.preventDefault();
                    handleClose(tag);
                }}
            >
                {tag}
            </Tag>
        );
        return (
            <span key={tag} style={{ display: 'inline-block' }}>
        {tagElem}
      </span>
        );
    };

    const tagChild = tags.map(forMap);

    const tagPlusStyle: React.CSSProperties = {
        background: token.colorBgContainer,
        borderStyle: 'dashed',
    };

    return <>
        <div style={{ marginBottom: 16 }}>
            <TweenOneGroup
                enter={{
                    scale: 0.8,
                    opacity: 0,
                    type: 'from',
                    duration: 100,
                }}
                onEnd={(e) =>
                {
                    if (e.type === 'appear' || e.type === 'enter')
                    {
                        (e.target as any).style = 'display: inline-block';
                    }
                }}
                leave={{ opacity: 0, width: 0, scale: 0, duration: 200 }}
                appear={false}
            >
                {tagChild}
            </TweenOneGroup>
        </div>
        {inputVisible ? (
            <Input
                ref={inputRef}
                type="text"
                size="small"
                style={{ width: 78 }}
                value={inputValue}
                onChange={handleInputChange}
                onBlur={handleInputConfirm}
                onPressEnter={handleInputConfirm}
            />
        ) : (
            <Tag onClick={showInput} style={tagPlusStyle}>
                <PlusOutlined/> 新增标签
            </Tag>
        )}
    </>
}

export default TagComponent