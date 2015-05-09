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
		<td>instanceof</td><td>int</td><td>loadfunc</td><td>new</td><td>null</td><td>override</td>
	</tr>
	<tr>
		<td>real</td><td>return</td><td>sizeof</td><td>string</td><td>struct</td><td>switch</td>
	</tr>
	<tr>
		<td>this</td><td>true</td><td>virtual</td><td>while</td>
	</tr>
</table>

### Basic Types: ###
<table>
		<tr>
			<th>name</th>
			<th>size</th>
			<th>init</th>
			<th>name</th>
			<th>size</th>
			<th>init</th>
		</tr>
		<tr>
			<td>char</td>
			<td>2</td>
			<td>'\0'</td>
			<td>string</td>
			<td>x</td>
			<td>""</td>
		</tr>
		<tr>
			<td>int</td>
			<td>4</td>
			<td>0i</td>
			<td>real</td>
			<td>4</td>
			<td>0.0r</td>
		</tr>
		<tr>
			<td>bigint</td>
			<td>x</td>
			<td>0I</td>
			<td>bigreal</td>
			<td>x</td>
			<td>0.0R</td>
		</tr>
		<tr>
			<td>bool</td>
			<td>1</td>
			<td>false</td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td>struct</td>
			<td>x</td>
			<td>null</td>
			<td>array</td>
			<td>x</td>
			<td>null</td>
		</tr>
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
* Function declaration & definition
	* Definition
		* Normal function definition
			1. `def` return-type func-name ( [para-type para-name,...] ){
			2. &emsp;&emsp; funtion-body
			3. }
		* Structure's member function definition(can use `this` variable in function body)
			1. `def` return-type structure-name.func-name ([para-type para-name,...]){
			2. &emsp;&emsp;function-body
 			3. }
	* Declaration(if not used,no need to implement it)
		- `def` return-type func-name([para-type para-name,...]);
* Structure declaration & definition :
	- Define a structure
		1.  `struct` name {
		2. &emsp;type-name var-name;[...] ***//Member declaration***
		3. &emsp;`def` return-type func-name([para-type para-name,...]);***//Function declaration***
		4. &emsp;`def` return-type func-name([para-type para-name,...]){***//Function definition***
		5. &emsp;&emsp;&emsp;&emsp;function-body;
		6. &emsp;&nbsp;}
		7. &nbsp;}
	- Inheriting and overriding
		1. Inherite a base structure
			- `struct` **derive-name**`:`**base-name**
		2. Virtual function
			- `def` `virtual` return-type func-name([para-type para-name,...]) 
		3. Override function
			- `def` `override` return-type func-name([para-type para-name,...]) 
		4. Override function must be virtual function in base struct
		5. If a struct(or its father) define a pure virtual function(declared but no definition),it can't be instantial
		6. 
	- Operand overloading
		- Definition grammar
			1.  `struct` name {
			2.  &emsp;@operand-name
			3.  &emsp;`def` return-type func-name([para-type para-name,...]);***//Function declaration or definition***
			4.  &nbsp;}
		- Constraint
			1. available operands have `+`,`-`,`*`,`/`,`%`,`>`,`<`,`<=`,`>=`, and types(except the self-type and array)
			2. operands `+`,`-`,`*`,`*` overloading functions should have&only have one parameter whose type is the same as the struct,and they returns the same type as the struct
			3. operands `<`,`>`,`<=`,`>=` overloading functions should have&only have one parameter whose type is the same as the struct,but the return-type should be `bool`
			4. the operands `==`,`!=` is built-in operation,they compared by the struct address,so we disabled their overloading to avoid confusion
			5. type-conversion function should have no parameter ant its return-type should be the same as the operand
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
		- Basic-type []... var-name\[,...](for array declaration)
	* Constant:
		- `int`:only decimal integer is supported(ends with '**i**' or less than IntMax)
		- `char`:'character'(character includes escape character `\n`,`\t`,`\r`,`\'`,`\"`,`\?`,`\b`,`\f`,`\\`)
		- `string`:"characters"
		- `real`:just support decimal real number(ends with '**r**' or less than RealMax)
		- `bool`:`true`,`false`
		- `bigint`:integer larger than IntMax(or ends with '**I**')
		- `bigreal`:real larger than RealMax(or ends with '**R**') 
		- `null`:used for array and struct
	* Dynamic array allocation:
		- `new` < type > (expression);
		- `new` <struct-type>;
	* Array size getter:(return an integer)
		- `sizeof` array-type-expression
	* Instance check:(return `true` or `false`)
		- expression `instanceof` basic-type
	* Built-in variable:
		- `_line_`:  line number of the source file
		- `_file_`:  file name of the source file
		- `_version_`: compiler version(major-version * 100 + minor-version)
		- `_args_`: command line arguments(runtime variable)
* Features in future
	* Inline function definition:
		- `def` return-type func-name([para-type para-name,...]) = expression;
