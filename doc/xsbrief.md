## XXXScript in brief ##

### Keyword List: ###

<table >
	<tr>
		<td>_file_</td><td>_line_</td><td>_version_</td><td>bigint</td><td>bigreal</td><td>bool</td>
	</tr>
	<tr>
		<td>break</td><td>case</td><td>char</td><td>continue</td><td>def</td><td>default</td>
	</tr>
	<tr>
		<td>do</td><td>else</td><td>false</td><td>for</td><td>if</td><td>import</td>
	</tr>
	<tr>
		<td>int</td><td>loadfunc</td><td>new</td><td>real</td><td>return</td><td>sizeof</td>
	</tr>
	<tr>
		<td>string</td><td>struct</td><td>switch</td><td>this</td><td>true</td><td>while</td>
	</tr>
</table>

### Basic Types: ###
<table>
		<tr>
			<th>name</th>
			<th>size</th>
			<th>name</th>
			<th>size</th>
		</tr>
		<tr>
			<td>char</td>
			<td>2</td>
			<td>string</td>
			<td>x</td>
		</tr>
		<tr>
			<td>int</td>
			<td>4</td>
			<td>real</td>
			<td>4</td>
		</tr>
		<tr>
			<td>bigint</td>
			<td>x</td>
			<td>bigreal</td>
			<td>x</td>
		</tr>
		<tr>
			<td>bool</td>
			<td>1</td>
		<tr>
</table>

### Grammar ###

* Loop statements:
	* `while`(bool-type expression) statement
	* `for`(def-expression;bool-type expression;expression) statement
	* `do` statement `while`(bool-type expression);
* Condition statement
	* `if`(bool-type expression) statement
	* `if`(bool-type expression) `else` statement
	*  switch statement
		1. `switch`(expression){**//expression should be int,char or string**
		2. &emsp;`case` constant-expression:**//expression should be constant**
		3. &emsp;&emsp;case-body;**//use `break` if jumping out is needed**
		3. &emsp;`default`:**//default case**
		4. &emsp;&emsp;case-body;
		4. }
	  
* Jump statement
	* `break`;(in loops or switch)
	* `continue`;(in loops)
	* `return` expression;(in functions)
* Function declaration & defination
	* Defination
		* Normal dunction defination
			1. `def` return-type func-name ( [para-type para-identifier,...] ){
			2. &emsp;&emsp; funtion-body
			3. }
		* Structure's member function defination(can use `this` variable in function body)
			1. `def` return-type structure-name.func-name ([para-type para-`,...]){
			2. &emsp;&emsp;function-body
 			3. }
	* Declaration(if not used,no need to implement it)
		- `def` return-type func-name([para-type para-name,...]);
* Structure declaration & definition :
	- Define a structure
		1.  `struct` name {
		2. &emsp;type-name var-name;[...] ***//Member declaration***
		3. &emsp;`def` return-type func-name([para-type para-name,...]);***//Function declaration***
		4. &emsp;`def` return-type func-name([para-type para-name,...]){***//Function defination***
		5. &emsp;&emsp;&emsp;&emsp;function-body;
		6. &emsp;&nbsp;}
		7. &nbsp;}
	- Structure member access:
		- name.member-name
* Load extension function:
	1. loadfunc<package-name>{
	2. &emsp;return-type func-name ( [para-type para-name,...] );
	3. }
* Import source file:
	- `import` "file-path";
* Expressions:
	* variable `+=` expression(variable's type can be `string`)
	* variable (`=`,`-=`,`*=`,`/=`,`%=`) expression(`%=` only for `int` & `char`)
	* cast expression
		- ( `type-name` ) expression
	* string/array access:
		- `var-expression` [expression]...
	* unary expression:
		- prefix:`-`,`!`,`++`,`--`
		- postfix:`++`,`--`
	* expression (`+`,`-`,`*`,`/`,`%`) expression(`%` for `int` & `char`)
		- expression (`==`,`!=`,`>`,`>=`,`<`,`<=`) expression
		- bool-type expression (`&&`,`||`) bool-type  expression
	* Variable declaration
		- Basic-type var-name\[,...] (var-name must begin with one of [a-zA-Z_])
		- Basic-type var-name = initial-value\[,...](with initial-value)
		- Basic-type [integer constant]... var-name\[,...](for array declaration)
	* Constant:
		- `int`:only decimal integer is supported(ends with '*i*' or less than IntMax)
		- `char`:'character'(character includes escape character `\n`,`\t`,`\r`,`\'`,`\"`,`\?`,`\b`,`\f`,`\\`)
		- `string`:"characters"
		- `real`:just support decimal real number(ends with '*r*' or less than RealMax)
		- `bool`:`true`,`false`
		- `bigint`:integer larger than IntMax(or ends with '*I*')
		- `bigreal`:real larger than RealMax(or ends with '*R*') 
	* Dynamic array allocation:
		- `new` < type > (expression);
	* Array size getter:
		- `sizeof` array-type-expression
	* Build-in variable:
		- `_line_`:  line number of the source file
		- `_file_`:  file name of the source file
		- `_version_`: compiler version(major-version * 100 + minor-version)
		- `_args_`: command line arguments(runtime variable)
* Features in future
	* Inline function definition:
		- `def` return-type func-name([para-type para-name,...]) = expression;