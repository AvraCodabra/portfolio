# Tal Ben Avraham -- 28/07/18
# Linked list.
# Registers used:

# $t0 - the address to the current node.
# $t8 - the number of the current node.
# $t9 - the address of the current node (to the next node).
# $t1 - the total sum.
# $t2 - temp.

# $v0 - syscall parameter.
# $a0 - syscall parameter.
# $at - Assembler Temporary.


			############ Data ############
.data

#the linked list
num1: .word -1 ,num2
num2: .word 32 ,num3
num3: .word 1972 ,num4
num4: .word -6 ,num5
num5: .word 17 , 0

#messages
A_msg: .asciiz "A. the sum of the link list's numbers is:  "
B_msg: .asciiz "B. the sum of positives divide by four is: "
C_msg: .asciiz "C. the output is: "
D_msg: .asciiz "D. the output is: "
str_enter: .asciiz "\n\n"

			############ A - print the sum of the linked list ############
.text
.globl main
main:
lw $t1,-2 ($4)
  			##section A##
  			
la $t0, num1 			# t0 = the link's root.
li $t1, 0 			# sum = 0
loopA:  lw $t8,($t0)		# t8 = node.num
	lw $t9, 4($t0)		# t9 = node.address
	add $t1, $t1, $t8	# sum += node.num
	beq $t9, $zero, endA	# end of link list, exit loop
	la $t0, ($t9)		#load next node
	j loopA
endA:
#print
la $a0, A_msg	
li $v0, 4			
syscall				#print message to section A 
move $a0, $t1	
li $v0, 1			
syscall				#print sum of section A 
la $a0, str_enter
li $v0, 4
syscall				#aesthetics


			## B - print the sum of the numbers that divided by 4 ##
			
la $t0, num1 			# t0 = the link's root.
li $t1, 0 			# sum = 0
loopB:  beq $t0, $zero, endB	# end of link list, exit loop
	lw $t8,($t0)		# t8 = node.num
	lw $t9, 4($t0)		# t9 = node.address
	andi $at,$t8,0x80000003 # if t8 is positives and divide by four then at = 0;
	bne  $at, $zero, noSum	# if at = 0 then next line, else go to noSum
	add $t1, $t1, $t8	# sum += node.num
noSum:	la $t0, ($t9)		#load next node
	j loopB
endB:
#print
la $a0, B_msg	
li $v0, 4			
syscall				#print message to section B
move $a0, $t1	
li $v0, 1			
syscall				#print sum of section B
la $a0, str_enter
li $v0, 4
syscall				#aesthetics


			## C - print the number as signed base-4 numbers ##
			
move $t2, $sp			# save the stack address
la $a0, C_msg	
li $v0, 4			
syscall				#print message of section C
la $t0, num1 			# t0 = the link's root.
loopC:  beq $t0, $zero, endC	# end of link list, exit loop
	lw $t8,($t0)		# t8 = node.num
	lw $t0, 4($t0)		# t0 = node.address
	bgez $t8 , loopC1	# if t8<0 print '-'
	li $a0, '-'
	li $v0, 11		
	syscall	
	abs $t8, $t8		# t8 = |t8|			
loopC1:	andi $a0, $t8, 3	#mask
	sb $a0, ($sp)		#storing the digits in a stack
	srl $t8, $t8, 2
	addi $sp, $sp, -1
	bnez $t8, loopC1
loopC2: addi $sp, $sp, 1	#print this string back from the stack
	lb $a0, ($sp)
	li $v0, 1
	syscall
	bne $sp, $t2 loopC2 
	li $a0, ','		#aesthetics
	li $v0, 11		
	syscall	
	j loopC
endC:

la $a0, str_enter
li $v0, 4
syscall				#aesthetics

			## D - print the numbers as unsigned base-4 numbers##
move $t2, $sp			# save the stack address
la $a0, D_msg	
li $v0, 4			
syscall				#print message of section D
la $t0, num1 			# t0 = the link's root.
loopD:  beq $t0, $zero, endD	# end of link list, exit loop
	lw $t8,($t0)		# t8 = node.num
	lw $t0, 4($t0)		# t0 = node.address
			
loopD1:	andi $a0, $t8, 3	#mask
	sb $a0, ($sp)		#storing the digits in a stack
	srl $t8, $t8, 2
	addi $sp, $sp, -1
	bnez $t8, loopD1
loopD2: addi $sp, $sp, 1	#print this string back from the stack
	lb $a0, ($sp)
	li $v0, 1
	syscall
	bne $sp, $t2 loopD2 
	li $a0, ','		#aesthetics
	li $v0, 11		
	syscall	
	j loopD
endD:

li   $v0,10
syscall				# end of program 
