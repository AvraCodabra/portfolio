# Tal Ben Avraham -- 03/08/18
# Question 5.
# Registers used:

# $s0 - Index
# $s1 - Index.instruction
# $s2 - Index.instruction.opcode
# $s3 - Index.instruction.rs
# $s4 - Index.instruction.rt
# $s5 - Index.instruction.rd
	
# $t0 - tmp
# $t1 - tmp
# $t2 - tmp
# $t3 - tmp

# $v0 - syscall parameter.
# $a0 - syscall parameter.
# $at - Assembler Temporary.

			############ Data ############
.data

count: .space 144 # (32+4)*4=144 , [reg0, reg1,...,reg31, rType ,lw ,sw ,req ]

TheCode: .word 0x10210000, 0XBBB82B,0X8E00BC90,0XAF5ED3E0,0XAC83FD25,0X8F12BD9B,0X8E188941,0X8D4615F6,0X8D951A03,0XAFD134AA,0X202C005,0X12A2192F,0XAC9209C3,0X26D6815,0X8F6B5BA9,0XAFCA513B,0X21A03E,0XAC0A0578,0X36D7818,0XACFE194C,0X107000F,0X8EA883D1,0X78E80C,0X1395416E,0XAE4182D9,0X23A831,0XACC4082E,0XACAF246A,0XAD2BE32D,0X123AAFFB,0X17F2804,0X11E0E4EE,0X37E5818,0X102BD4A6,0X1157D015,0X8CD32505,0X8EE23635,0X384580C,0X8F20CFB7,0XAE0F2CAF,0X286E03B,0xffffffff

#messages
msg_eOpcode: .asciiz "one or more of the instruction has an invalid Opcode \n"
msg_rtZerolw: .asciiz "rt is equal to zero in an lw instruction \n"
msg_rdZeroR: .asciiz "rd is equal to zero in an R-type instruction \n"
msg_rsBEQrt: .asciiz "rs is equal to rt in an beq instruction \n"

print_head: .asciiz "inst code/reg \t appearances \n"
print_Rtype: .asciiz "R-type \t\t\t"
print_lw: .asciiz "lw \t\t\t"
print_sw: .asciiz "sw \t\t\t"
print_beq: .asciiz "beq \t\t\t"
print_t: .asciiz "\t\t\t"
print_n: .asciiz "\n"

			############ Text ############
.text
.globl main
main:

la $t3, count
li $t2, 0
resC:	sw   $zero, ($t3)	# loop to reset the counter
	addi $t2, $t2, 1
	addu $t3, $t3, 4
	ble  $t2, 0x23, resC 		


la $s0, TheCode			#index = 0
lw $s1, ($s0)			#index.instruction 
beq $s1 ,0xFFFFFFFF, endloop
loop:

    ##set the values of the instruction in the regs##

			 	# "while index.instruction  not 0xffffffff"
srl $s2, $s1, 26		# s2 = OpCode
srl $s3, $s1, 21		# s3 = rs
andi $s3, $s3, 31		#mask
srl $s4, $s1, 16		# s4 = rt
andi $s4, $s4, 31		#mask
andi $s5, $s1, 0xffff		#mask
srl  $s5, $s5, 11		#s5 = rd (when R-Type)

    		##check the OpCode##
    		
beqz $s2, rType			#if opcood = R-Type then go to rType
addi $t2, $zero, 0x23
beq $s2, $t2, lwInst		#if opcood = lw then go to lwInst
addi $t2, $zero, 0x2b
beq $s2, $t2, swInst		#if opcood = sw then go to swInst
addi $t2, $zero, 0x4
beq $s2, $t2, beqInst		#if opcood = beq then go to beqInst
#else (invalid opcode)
la $a0, msg_eOpcode
li   $v0,4
syscall				#print  Opcode error msg
j error
    		##count for each case##
rType:
	lw   $t2, count+128
	addi $t2, $t2, 1
	sw $t2, count+128  	#count.rType++
	jal countRsRt		#count rs and rt
	jal countRd		#count rd
	bnez $s5, else1
		la $a0, msg_rdZeroR
		li $v0,4
		syscall		#print rd is equal to Zero msg
	else1:	j continue

lwInst:
	lw   $t2, count+132
	addi $t2, $t2, 1
	sw $t2, count+132  	#count.lwInst:++
	jal countRsRt		#count rs and rt
	bnez $s4, else2
		la $a0, msg_rtZerolw
		li $v0,4
		syscall		#print rt is equal to Zero msg
	else2:	j continue
	
swInst:
	lw   $t2, count+136
	addi $t2, $t2, 1
	sw $t2, count+136  	#count.swInst++
	jal countRsRt		#count rs and rt
	j continue
	
beqInst:
	lw   $t2, count+140
	addi $t2, $t2, 1
	sw $t2, count+140  	#count.beqInst++
	jal countRsRt		#count rs and rt
	bne $s3,$s4, else3
		la $a0, msg_rsBEQrt
		li $v0,4
		syscall		#print rs is equal to rd msg
	else3:

continue:

addi $s0, $s0, 4		#index++
lw $s1, ($s0)			#instruction++
bne $s1, 0xFFFFFFFF, loop
endloop:

jal finalPrint

error:
li   $v0,10
syscall				# end of program 


############################## functions ##############################
	
#count rs & rt function
countRsRt:			
	bge $s3, 0x20, elseRs	#if rs<32 then
	sll $t0, $s3, 2		#tmp0 = the reg offset from 'count'
	lw   $t1, count+0($t0)	
	addi $t1, $t1, 1
	sw $t1, count+0($t0)  	#count.reg[rs]++
elseRs: bge $s4, 0x20, elseRt	#if rt<32 then
	sll $t0, $s4, 2		#tmp0 = the reg offset from 'count'
	lw   $t1, count+0($t0)
	addi $t1, $t1, 1
	sw $t1, count+0($t0)  	#count.reg[rt]++
elseRt: jr $ra	

#count rd function	
countRd:
	bge $s5, 0x20, elseRd	#if rd<32 then
	sll $t0, $s5, 2		#tmp0 = the reg offset from 'count'
	lw   $t1, count+0($t0)	
	addi $t1, $t1, 1
	sw $t1, count+0($t0)  	#count.reg[rs]++
elseRd: jr $ra

#final print function	
finalPrint:
	la $a0, print_head
	li $v0,4
	syscall
	
	la $a0, print_Rtype
	li $v0,4
	syscall
	lw  $a0, count+128
	li $v0,1
	syscall
	la $a0, print_n
	li $v0,4
	syscall	
	
	la $a0, print_lw
	li $v0,4
	syscall
	lw  $a0, count+132
	li $v0,1
	syscall
	la $a0, print_n
	li $v0,4
	syscall	

	la $a0, print_sw
	li $v0,4
	syscall
	lw  $a0, count+136
	li $v0,1
	syscall
	la $a0, print_n
	li $v0,4
	syscall	
	
	la $a0, print_beq
	li $v0,4
	syscall
	lw  $a0, count+140
	li $v0,1
	syscall
	la $a0, print_n
	li $v0,4
	syscall	
	
	add $t0, $zero, $zero
loopP:	sll $t2, $t0, 2
	lw $t1, count+0($t2)
	beqz $t1 next
	
	move $a0, $t0
	li $v0,1
	syscall
	la $a0, print_t
	li $v0,4
	syscall
	move $a0, $t1
	li $v0,1
	syscall
	la $a0, print_n
	li $v0,4
	syscall
	
next:	addi $t0, $t0,1
	bne $t0, 0x20, loopP
	jr $ra
	
	
