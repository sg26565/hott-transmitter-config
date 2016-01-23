<#macro reset><#assign row="even"/></#macro>

<#macro d><#if row=="even">d0<#assign row="odd"/><#else>d1<#assign row="even"/></#if></#macro>

<#macro u b><#if b>used<#else>unused</#if></#macro>