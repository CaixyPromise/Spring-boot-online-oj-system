import React, {useEffect, useRef, useState} from "react";
import {Button, Card, Divider, Input, InputNumber, InputRef, message, Select, Space, Tag, Tooltip} from "antd";
import {ProForm, ProFormInstance} from "@ant-design/pro-form";
import {EditableProTable, PageContainer, ProColumns, ProFormText} from "@ant-design/pro-components";
import {PlusOutlined} from "@ant-design/icons";
import {MdEditor} from "md-editor-rt";
import 'md-editor-rt/lib/style.css';
import {getUUID} from "rc-select/es/hooks/useId";
import {addQuestionUsingPost1, updateQuestionUsingPost1} from "@/services/onlineJudge/adminBackend/questionController";
import {getQuestionVoByIdUsingGet1} from "@/services/onlineJudge/questionBackend/questionController";
import EditableTag from "@/components/EditableTag";
import {getLangProvideListUsingGet1} from "@/services/questionBackend/judgeController";
import {OptionArray} from "@/typings";

type CreateModalProps = {
}

type JudgeCase = {
    id: string | number;
    input: string;
    output: string;
}

const columns: ProColumns<JudgeCase>[] = [
    {
        title: '输入用例',
        dataIndex: 'input',
        valueType: 'textarea',
        width: '40%',
    },
    {
        title: '输出用例',
        dataIndex: 'output',
        valueType: 'textarea',
        width: '40%',
    },
    {
        title: '操作',
        valueType: 'option',
    },
];

const Index: React.FC<CreateModalProps> = ()=>{
    const formRef = useRef<ProFormInstance>();

    const [tags, setTags] = useState<string[]>([]);

    const [difficulty, setDifficulty] = useState<string>('简单');

    const [content, setContent] = useState<string>('')
    const [answer, setAnswer] = useState<string>('')

    const [editableKeys, setEditableRowKeys] = useState<React.Key[]>([]);

    const [timeLimit, setTimeLimit] = useState<number | null>(1000);
    const [memoryLimit, setMemoryLimit] = useState<number | null>(128);
    const [stackLimit, setStackLimit] = useState<number | null>(128);
    const [langList, setLangList] = useState<OptionArray<string>>([]);

    const [selectedItems, setSelectedItems] = useState<string[]>([]);

    //清空输入
    const clearInput = ()=>{
        formRef.current?.setFieldValue('title', '');
        setTags([]);
        setDifficulty('简单');
        setContent('');
        setAnswer('');
        formRef.current?.setFieldValue("judgeCase", []);
        setEditableRowKeys([]);
        setTimeLimit(1000);
        setMemoryLimit(128);
        setStackLimit(128);
    }

    //创建或更新题目
    const createUpdateProblem = ()=>{
        const params = {
            title: formRef.current?.getFieldValue('title'),
            tags,
            difficulty,
            content,
            answer,
            judgeCase: formRef.current?.getFieldValue('judgeCase').map((item: JudgeCase) => {
                return {
                    input: item.input,
                    output: item.output,
                }
            }),
            judgeConfig: {
                timeLimit,
                memoryLimit,
                stackLimit,
            }
        };

        console.log(params);
    }

    useEffect(() =>
    {
        getLangProvideListUsingGet1().then((res) => {
            setLangList(res.data as OptionArray<string>)
        })
    }, []);


    return <PageContainer>
        <Card bordered={false} >
            <div >
                <ProForm<API.QuestionAddRequest>
                    formRef={formRef}
                    labelCol={{span: 2}}
                    wrapperCol={{span: 22}}
                    labelAlign='left'
                    layout='horizontal'
                    submitter={false}
                >
                    <ProFormText name="title" label="标题" placeholder="请输入标题"/>

                    <ProForm.Item label="标签" name="tags">
                        <EditableTag tags={tags} setTags={setTags} />
                    </ProForm.Item>

                    <ProForm.Item label="难度" name="difficulty">
                        <Select
                            value={difficulty}
                            onChange={setDifficulty}
                            options={[
                                {value: '简单', label: '简单'},
                                {value: '中等', label: '中等'},
                                {value: '困难', label: '困难'},
                            ]}
                        />
                    </ProForm.Item>

                    <ProForm.Item label="语言" name="lang">
                        <Select
                            mode="multiple"
                            value={selectedItems}
                            onChange={setSelectedItems}
                            allowClear
                            dropdownRender={(menu) => (
                                <>
                                    {menu}
                                    <Divider style={{ margin: '8px 0' }} />
                                    <Button
                                        type="link"
                                        onClick={() => {
                                            setSelectedItems(() => {
                                                return langList.map((item) => item.value)
                                            });
                                        }}
                                        style={{ width: '100%', textAlign: 'center' }}
                                    >选择全部</Button>
                                </>
                            )}
                            options={langList}
                        />
                    </ProForm.Item>

                    <ProForm.Item label="题目描述" name="content">
                        <MdEditor
                            style={{height: 600}}
                            modelValue={content}
                            editorId='content'
                            toolbarsExclude={['save', 'htmlPreview', 'github']}
                            onChange={(newContent) =>
                            {
                                setContent(newContent)
                            }}
                        />
                    </ProForm.Item>

                    <ProForm.Item label="题解" name="content">
                        <MdEditor
                            style={{height: 600}}
                            modelValue={answer}
                            editorId='answer'
                            toolbarsExclude={['save', 'htmlPreview', 'github']}
                            onChange={(newContent) =>
                            {
                                setAnswer(newContent)
                            }}
                        />
                    </ProForm.Item>

                    <ProForm.Item
                        label="测试用例"
                        name="judgeCase"
                        initialValue={[]}
                        trigger="onValuesChange"
                    >
                        <EditableProTable<JudgeCase>
                            rowKey="id"
                            toolBarRender={false}
                            columns={columns}
                            recordCreatorProps={{
                                newRecordType: 'dataSource',
                                position: 'bottom',
                                record: () => ({
                                    id: getUUID(),
                                    input: '',
                                    output: ''
                                }),
                            }}
                            editable={{
                                type: 'multiple',
                                editableKeys,
                                onChange: setEditableRowKeys,
                                actionRender: (row, _, dom) =>
                                {
                                    return [dom.delete];
                                },
                            }}
                        />
                    </ProForm.Item>

                    <ProForm.Item label="时间限制" name="timeLimit">
                        <InputNumber addonAfter="ms" value={timeLimit} onChange={(value) => setTimeLimit(value)}/>
                    </ProForm.Item>

                    <ProForm.Item label="内存限制" name="memoryLimit">
                        <InputNumber addonAfter="MB" value={memoryLimit} onChange={(value) => setMemoryLimit(value)}/>
                    </ProForm.Item>

                    <ProForm.Item label="堆栈限制" name="stackLimit">
                        <InputNumber addonAfter="MB" value={stackLimit} onChange={(value) => setStackLimit(value)}/>
                    </ProForm.Item>
                </ProForm>
            </div>
        </Card>
    </PageContainer>
}

export default Index;

