package com.czeta.onlinejudgecore.consts;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName JudgeMachineConst
 * @Description
 * @Author chenlongjie
 * @Date 2020/4/9 18:19
 * @Version 1.0
 */
@Data
public class JudgeMachineConst {
    /*
[
    {
        "spj":{
            "config":{
                "command":"{exe_path} {in_file_path} {user_out_file_path}",
                "exe_name":"spj-{spj_version}",
                "seccomp_rule":"c_cpp"
            },
            "compile":{
                "exe_name":"spj-{spj_version}",
                "src_name":"spj-{spj_version}.c",
                "max_memory":1073741824,
                "max_cpu_time":3000,
                "max_real_time":10000,
                "compile_command":"/usr/bin/gcc -DONLINE_JUDGE -O2 -w -fmax-errors=3 -std=c11 {src_path} -lm -o {exe_path}"
            }
        },
        "name":"C",
        "config":{
            "run":{
                "env":[
                    "LANG=en_US.UTF-8",
                    "LANGUAGE=en_US:en",
                    "LC_ALL=en_US.UTF-8"
                ],
                "command":"{exe_path}",
                "seccomp_rule":{
                    "File IO":"c_cpp_file_io",
                    "Standard IO":"c_cpp"
                }
            },
            "compile":{
                "exe_name":"main",
                "src_name":"main.c",
                "max_memory":268435456,
                "max_cpu_time":3000,
                "max_real_time":10000,
                "compile_command":"/usr/bin/gcc -DONLINE_JUDGE -O2 -w -fmax-errors=3 -std=c11 {src_path} -lm -o {exe_path}"
            }
        },
        "description":"GCC 5.4",
        "content_type":"text/x-csrc"
    },
    {
        "spj":{
            "config":{
                "command":"{exe_path} {in_file_path} {user_out_file_path}",
                "exe_name":"spj-{spj_version}",
                "seccomp_rule":"c_cpp"
            },
            "compile":{
                "exe_name":"spj-{spj_version}",
                "src_name":"spj-{spj_version}.cpp",
                "max_memory":1073741824,
                "max_cpu_time":3000,
                "max_real_time":5000,
                "compile_command":"/usr/bin/g++ -DONLINE_JUDGE -O2 -w -fmax-errors=3 -std=c++14 {src_path} -lm -o {exe_path}"
            }
        },
        "name":"C++",
        "config":{
            "run":{
                "env":[
                    "LANG=en_US.UTF-8",
                    "LANGUAGE=en_US:en",
                    "LC_ALL=en_US.UTF-8"
                ],
                "command":"{exe_path}",
                "seccomp_rule":"c_cpp"
            },
            "compile":{
                "exe_name":"main",
                "src_name":"main.cpp",
                "max_memory":536870912,
                "max_cpu_time":3000,
                "max_real_time":10000,
                "compile_command":"/usr/bin/g++ -DONLINE_JUDGE -O2 -w -fmax-errors=3 -std=c++14 {src_path} -lm -o {exe_path}"
            }
        },
        "description":"G++ 5.4",
        "content_type":"text/x-c++src"
    },
    {
        "name":"Java",
        "config":{
            "run":{
                "env":[
                    "LANG=en_US.UTF-8",
                    "LANGUAGE=en_US:en",
                    "LC_ALL=en_US.UTF-8"
                ],
                "command":"/usr/bin/java -cp {exe_dir} -XX:MaxRAM={max_memory}k -Djava.security.manager -Dfile.encoding=UTF-8 -Djava.security.policy==/etc/java_policy -Djava.awt.headless=true Main",
                "seccomp_rule":null,
                "memory_limit_check_only":1
            },
            "compile":{
                "exe_name":"Main",
                "src_name":"Main.java",
                "max_memory":-1,
                "max_cpu_time":3000,
                "max_real_time":5000,
                "compile_command":"/usr/bin/javac {src_path} -d {exe_dir} -encoding UTF8"
            },
            "template":"//PREPEND BEGIN
//PREPEND END

//TEMPLATE BEGIN
//TEMPLATE END

//APPEND BEGIN
//APPEND END"
        },
        "description":"OpenJDK 1.8",
        "content_type":"text/x-java"
    },
    {
        "name":"Python2",
        "config":{
            "run":{
                "env":[
                    "LANG=en_US.UTF-8",
                    "LANGUAGE=en_US:en",
                    "LC_ALL=en_US.UTF-8"
                ],
                "command":"/usr/bin/python {exe_path}",
                "seccomp_rule":"general"
            },
            "compile":{
                "exe_name":"solution.pyc",
                "src_name":"solution.py",
                "max_memory":134217728,
                "max_cpu_time":3000,
                "max_real_time":10000,
                "compile_command":"/usr/bin/python -m py_compile {src_path}"
            }
        },
        "description":"Python 2.7",
        "content_type":"text/x-python"
    },
    {
        "name":"Python3",
        "config":{
            "run":{
                "env":[
                    "LANG=en_US.UTF-8",
                    "LANGUAGE=en_US:en",
                    "LC_ALL=en_US.UTF-8",
                    "PYTHONIOENCODING=utf-8",
                    "PYTHONIOENCODING=utf-8"
                ],
                "command":"/usr/bin/python3 {exe_path}",
                "seccomp_rule":"general"
            },
            "compile":{
                "exe_name":"__pycache__/solution.cpython-35.pyc",
                "src_name":"solution.py",
                "max_memory":134217728,
                "max_cpu_time":3000,
                "max_real_time":10000,
                "compile_command":"/usr/bin/python3 -m py_compile {src_path}"
            }
        },
        "description":"Python 3.5",
        "content_type":"text/x-python"
    }
]
     */

    public static final String LANGUAGE_JSON_STR = "[{\"spj\":{\"config\":{\"command\":\"{exe_path} {in_file_path} {user_out_file_path}\",\"exe_name\":\"spj-{spj_version}\",\"seccomp_rule\":\"c_cpp\"},\"compile\":{\"exe_name\":\"spj-{spj_version}\",\"src_name\":\"spj-{spj_version}.c\",\"max_memory\":1073741824,\"max_cpu_time\":3000,\"max_real_time\":10000,\"compile_command\":\"/usr/bin/gcc -DONLINE_JUDGE -O2 -w -fmax-errors=3 -std=c11 {src_path} -lm -o {exe_path}\"}},\"name\":\"C\",\"config\":{\"run\":{\"env\":[\"LANG=en_US.UTF-8\",\"LANGUAGE=en_US:en\",\"LC_ALL=en_US.UTF-8\"],\"command\":\"{exe_path}\",\"seccomp_rule\":{\"File IO\":\"c_cpp_file_io\",\"Standard IO\":\"c_cpp\"}},\"compile\":{\"exe_name\":\"main\",\"src_name\":\"main.c\",\"max_memory\":268435456,\"max_cpu_time\":3000,\"max_real_time\":10000,\"compile_command\":\"/usr/bin/gcc -DONLINE_JUDGE -O2 -w -fmax-errors=3 -std=c11 {src_path} -lm -o {exe_path}\"}},\"description\":\"GCC 5.4\",\"content_type\":\"text/x-csrc\"},{\"spj\":{\"config\":{\"command\":\"{exe_path} {in_file_path} {user_out_file_path}\",\"exe_name\":\"spj-{spj_version}\",\"seccomp_rule\":\"c_cpp\"},\"compile\":{\"exe_name\":\"spj-{spj_version}\",\"src_name\":\"spj-{spj_version}.cpp\",\"max_memory\":1073741824,\"max_cpu_time\":10000,\"max_real_time\":20000,\"compile_command\":\"/usr/bin/g++ -DONLINE_JUDGE -O2 -w -fmax-errors=3 -std=c++14 {src_path} -lm -o {exe_path}\"}},\"name\":\"C++\",\"config\":{\"run\":{\"env\":[\"LANG=en_US.UTF-8\",\"LANGUAGE=en_US:en\",\"LC_ALL=en_US.UTF-8\"],\"command\":\"{exe_path}\",\"seccomp_rule\":{\"File IO\":\"c_cpp_file_io\",\"Standard IO\":\"c_cpp\"}},\"compile\":{\"exe_name\":\"main\",\"src_name\":\"main.cpp\",\"max_memory\":1073741824,\"max_cpu_time\":10000,\"max_real_time\":20000,\"compile_command\":\"/usr/bin/g++ -DONLINE_JUDGE -O2 -w -fmax-errors=3 -std=c++14 {src_path} -lm -o {exe_path}\"}},\"description\":\"G++ 5.4\",\"content_type\":\"text/x-c++src\"},{\"name\":\"Java\",\"config\":{\"run\":{\"env\":[\"LANG=en_US.UTF-8\",\"LANGUAGE=en_US:en\",\"LC_ALL=en_US.UTF-8\"],\"command\":\"/usr/bin/java -cp {exe_dir} -XX:MaxRAM={max_memory}k -Djava.security.manager -Dfile.encoding=UTF-8 -Djava.security.policy==/etc/java_policy -Djava.awt.headless=true Main\",\"seccomp_rule\":null,\"memory_limit_check_only\":1},\"compile\":{\"exe_name\":\"Main\",\"src_name\":\"Main.java\",\"max_memory\":-1,\"max_cpu_time\":5000,\"max_real_time\":10000,\"compile_command\":\"/usr/bin/javac {src_path} -d {exe_dir} -encoding UTF8\"}},\"description\":\"OpenJDK 1.8\",\"content_type\":\"text/x-java\"},{\"name\":\"Python2\",\"config\":{\"run\":{\"env\":[\"LANG=en_US.UTF-8\",\"LANGUAGE=en_US:en\",\"LC_ALL=en_US.UTF-8\"],\"command\":\"/usr/bin/python {exe_path}\",\"seccomp_rule\":\"general\"},\"compile\":{\"exe_name\":\"solution.pyc\",\"src_name\":\"solution.py\",\"max_memory\":134217728,\"max_cpu_time\":3000,\"max_real_time\":10000,\"compile_command\":\"/usr/bin/python -m py_compile {src_path}\"}},\"description\":\"Python 2.7\",\"content_type\":\"text/x-python\"},{\"name\":\"Python3\",\"config\":{\"run\":{\"env\":[\"LANG=en_US.UTF-8\",\"LANGUAGE=en_US:en\",\"LC_ALL=en_US.UTF-8\"],\"command\":\"/usr/bin/python3 {exe_path}\",\"seccomp_rule\":\"general\"},\"compile\":{\"exe_name\":\"__pycache__/solution.cpython-35.pyc\",\"src_name\":\"solution.py\",\"max_memory\":134217728,\"max_cpu_time\":3000,\"max_real_time\":10000,\"compile_command\":\"/usr/bin/python3 -m py_compile {src_path}\"}},\"description\":\"Python 3.5\",\"content_type\":\"text/x-python\"}]";
    public static final Map<String, JSONObject> LANGUAGE_JSON_MAP = new HashMap<>();

    public static final String JUDGE_SERVER_TOKEN = "X-Judge-Server-Token";

    static {
        JSONArray jsonArray = JSONArray.parseArray(JudgeMachineConst.LANGUAGE_JSON_STR);;
        for (int i = 0; i < jsonArray.size(); ++i) {
            JSONObject obj = jsonArray.getJSONObject(i);
            LANGUAGE_JSON_MAP.put((String) obj.get("name"), obj);
        }
    }

    public static JSONArray getLanguageJsonArray() {
        return JSONArray.parseArray(JudgeMachineConst.LANGUAGE_JSON_STR);
    }

    public static JSONObject getLanguageConfigByName(String languageName) {
        return LANGUAGE_JSON_MAP.get(languageName).getJSONObject("config");
    }

    public static JSONObject getSpjConfigByName(String languageName) {
        return LANGUAGE_JSON_MAP.get(languageName).getJSONObject("spj").getJSONObject("config");
    }

    public static JSONObject getSpjCompileConfigByName(String languageName) {
        return LANGUAGE_JSON_MAP.get(languageName).getJSONObject("spj").getJSONObject("compile");
    }
}
