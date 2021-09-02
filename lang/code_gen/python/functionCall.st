functionCall(functionName, params, return, index) ::= <<
<if(return)><return; separator=", "> = <endif><functionName>(<params>)<if(index)>[<index>]<endif>
>>