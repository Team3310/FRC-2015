VERSION 5.00
Begin VB.Form frmDynamics 
   Caption         =   "P-50 Dynamics Input"
   ClientHeight    =   8385
   ClientLeft      =   60
   ClientTop       =   345
   ClientWidth     =   9075
   ControlBox      =   0   'False
   BeginProperty Font 
      Name            =   "Comic Sans MS"
      Size            =   8.25
      Charset         =   0
      Weight          =   400
      Underline       =   0   'False
      Italic          =   0   'False
      Strikethrough   =   0   'False
   EndProperty
   Icon            =   "frmP50Dynamics.frx":0000
   KeyPreview      =   -1  'True
   LinkTopic       =   "Form1"
   MaxButton       =   0   'False
   MinButton       =   0   'False
   ScaleHeight     =   8385
   ScaleWidth      =   9075
   StartUpPosition =   3  'Windows Default
   Begin VB.Frame frmGravity 
      Caption         =   "Gravity Direction"
      Height          =   975
      Left            =   6720
      TabIndex        =   133
      Top             =   3360
      Width           =   1935
      Begin VB.OptionButton optWall 
         Caption         =   "Wall Mount"
         Height          =   255
         Left            =   240
         TabIndex        =   135
         Top             =   600
         Width           =   1215
      End
      Begin VB.OptionButton optFloor 
         Caption         =   "Floor Mount"
         Height          =   255
         Left            =   240
         TabIndex        =   134
         Top             =   240
         Value           =   -1  'True
         Width           =   1215
      End
   End
   Begin VB.CheckBox chkWantTorque 
      Caption         =   "Output Joint Torques v. Time"
      Height          =   255
      Left            =   4920
      TabIndex        =   132
      Top             =   5520
      Value           =   1  'Checked
      Width           =   2655
   End
   Begin VB.CheckBox chkGrounded 
      Caption         =   "Axis 3 grounded ( Four Bar)"
      Height          =   255
      Left            =   4920
      TabIndex        =   131
      Top             =   5160
      Value           =   1  'Checked
      Width           =   3015
   End
   Begin VB.CheckBox chkWantForce 
      Caption         =   "Output Axis Forces and Moments v. Time"
      Height          =   255
      Left            =   4920
      TabIndex        =   130
      Top             =   5880
      Width           =   3495
   End
   Begin VB.CommandButton cmdRestore 
      Caption         =   "Restore Defaults"
      Height          =   375
      Left            =   4440
      TabIndex        =   129
      Top             =   7920
      Width           =   1455
   End
   Begin VB.TextBox txtLength 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   3
      Left            =   5520
      MousePointer    =   3  'I-Beam
      TabIndex        =   124
      Text            =   "500"
      Top             =   3120
      Width           =   615
   End
   Begin VB.TextBox txtLength 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   2
      Left            =   3120
      MousePointer    =   3  'I-Beam
      TabIndex        =   123
      Text            =   "500"
      Top             =   3120
      Width           =   615
   End
   Begin VB.OptionButton optDynamics 
      Caption         =   "Calculate Motion and Dynamics"
      Height          =   255
      Left            =   4560
      TabIndex        =   122
      Top             =   4800
      Width           =   3015
   End
   Begin VB.OptionButton optMotion 
      Caption         =   "Calculate Motion Only"
      Height          =   255
      Left            =   4560
      TabIndex        =   121
      Top             =   4440
      Value           =   -1  'True
      Width           =   2655
   End
   Begin VB.CommandButton cmdCancel 
      Cancel          =   -1  'True
      Caption         =   "Cancel"
      Height          =   375
      Left            =   6120
      TabIndex        =   31
      Top             =   7920
      Width           =   1215
   End
   Begin VB.TextBox txtIxx 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   4
      Left            =   7800
      TabIndex        =   26
      Text            =   "0"
      Top             =   2040
      Width           =   615
   End
   Begin VB.TextBox txtIyy 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   4
      Left            =   7800
      TabIndex        =   27
      Text            =   "0"
      Top             =   2400
      Width           =   615
   End
   Begin VB.TextBox txtIxx 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   3
      Left            =   5520
      TabIndex        =   19
      Text            =   "0"
      Top             =   2040
      Width           =   615
   End
   Begin VB.TextBox txtIyy 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   3
      Left            =   5520
      TabIndex        =   20
      Text            =   "0"
      Top             =   2400
      Width           =   615
   End
   Begin VB.TextBox txtIxx 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   2
      Left            =   3120
      TabIndex        =   12
      Text            =   "0"
      Top             =   2040
      Width           =   615
   End
   Begin VB.TextBox txtIyy 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   2
      Left            =   3120
      TabIndex        =   13
      Text            =   "0"
      Top             =   2400
      Width           =   615
   End
   Begin VB.TextBox txtIxx 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   1
      Left            =   720
      TabIndex        =   5
      Text            =   "0"
      Top             =   2040
      Width           =   615
   End
   Begin VB.TextBox txtIyy 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   1
      Left            =   720
      TabIndex        =   6
      Text            =   "0"
      Top             =   2400
      Width           =   615
   End
   Begin VB.TextBox txtCGz 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   4
      Left            =   7800
      TabIndex        =   25
      Text            =   "0"
      Top             =   1680
      Width           =   615
   End
   Begin VB.TextBox txtCGz 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   3
      Left            =   5520
      TabIndex        =   18
      Text            =   "0"
      Top             =   1680
      Width           =   615
   End
   Begin VB.TextBox txtCGz 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   2
      Left            =   3120
      TabIndex        =   11
      Text            =   "0"
      Top             =   1680
      Width           =   615
   End
   Begin VB.TextBox txtCGz 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   1
      Left            =   720
      TabIndex        =   4
      Text            =   "0"
      Top             =   1680
      Width           =   615
   End
   Begin VB.TextBox txtMass 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   1
      Left            =   720
      TabIndex        =   1
      Text            =   "0"
      Top             =   600
      Width           =   615
   End
   Begin VB.TextBox txtCGx 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   1
      Left            =   720
      TabIndex        =   2
      Text            =   "0"
      Top             =   960
      Width           =   615
   End
   Begin VB.TextBox txtCGy 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   1
      Left            =   720
      TabIndex        =   3
      Text            =   "0"
      Top             =   1320
      Width           =   615
   End
   Begin VB.TextBox txtIzz 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   1
      Left            =   720
      MousePointer    =   3  'I-Beam
      TabIndex        =   7
      Text            =   "0"
      Top             =   2760
      Width           =   615
   End
   Begin VB.Frame axis3Config 
      Caption         =   "Axis 3 Configuration"
      Height          =   1095
      Left            =   2280
      TabIndex        =   66
      Top             =   7200
      Width           =   1935
      Begin VB.OptionButton optElbowUp 
         Caption         =   "Elbow Up"
         Height          =   255
         Left            =   120
         TabIndex        =   68
         Top             =   360
         Value           =   -1  'True
         Width           =   1095
      End
      Begin VB.OptionButton optElbowDown 
         Caption         =   "Elbow Down"
         Height          =   255
         Left            =   120
         TabIndex        =   67
         Top             =   720
         Width           =   1335
      End
   End
   Begin VB.Frame axis1Config 
      Caption         =   "Axis 1 Configuration"
      Height          =   1095
      Left            =   120
      TabIndex        =   63
      Top             =   7200
      Width           =   1935
      Begin VB.OptionButton optBackReach 
         Caption         =   "Back Reach"
         Height          =   255
         Left            =   240
         TabIndex        =   65
         Top             =   720
         Width           =   1455
      End
      Begin VB.OptionButton optFrontReach 
         Caption         =   "Front Reach"
         Height          =   255
         Left            =   240
         TabIndex        =   64
         Top             =   360
         Value           =   -1  'True
         Width           =   1335
      End
   End
   Begin VB.CommandButton cmdDynamics 
      Caption         =   "Continue"
      Default         =   -1  'True
      Height          =   375
      Left            =   7560
      TabIndex        =   29
      Top             =   7920
      Width           =   1215
   End
   Begin VB.PictureBox picRobot 
      Appearance      =   0  'Flat
      BackColor       =   &H80000005&
      ForeColor       =   &H80000008&
      Height          =   3135
      Left            =   120
      Picture         =   "frmP50Dynamics.frx":0442
      ScaleHeight     =   2.156
      ScaleMode       =   5  'Inch
      ScaleWidth      =   2.823
      TabIndex        =   45
      TabStop         =   0   'False
      Top             =   3960
      Width           =   4095
   End
   Begin VB.TextBox txtIzz 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   4
      Left            =   7800
      MousePointer    =   3  'I-Beam
      TabIndex        =   28
      Text            =   "0"
      Top             =   2760
      Width           =   615
   End
   Begin VB.TextBox txtCGy 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   4
      Left            =   7800
      TabIndex        =   24
      Text            =   "0"
      Top             =   1320
      Width           =   615
   End
   Begin VB.TextBox txtCGx 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   4
      Left            =   7800
      TabIndex        =   23
      Text            =   "0"
      Top             =   960
      Width           =   615
   End
   Begin VB.TextBox txtMass 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   4
      Left            =   7800
      TabIndex        =   22
      Text            =   "0"
      Top             =   600
      Width           =   615
   End
   Begin VB.TextBox txtIzz 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   3
      Left            =   5520
      MousePointer    =   3  'I-Beam
      TabIndex        =   21
      Text            =   "0"
      Top             =   2760
      Width           =   615
   End
   Begin VB.TextBox txtCGy 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   3
      Left            =   5520
      TabIndex        =   17
      Text            =   "0"
      Top             =   1320
      Width           =   615
   End
   Begin VB.TextBox txtCGx 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   3
      Left            =   5520
      TabIndex        =   16
      Text            =   "0"
      Top             =   960
      Width           =   615
   End
   Begin VB.TextBox txtMass 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   3
      Left            =   5520
      TabIndex        =   15
      Text            =   "0"
      Top             =   600
      Width           =   615
   End
   Begin VB.TextBox txtIzz 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   2
      Left            =   3120
      MousePointer    =   3  'I-Beam
      TabIndex        =   14
      Text            =   "0"
      Top             =   2760
      Width           =   615
   End
   Begin VB.TextBox txtCGy 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   2
      Left            =   3120
      TabIndex        =   10
      Text            =   "0"
      Top             =   1320
      Width           =   615
   End
   Begin VB.TextBox txtCGx 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   2
      Left            =   3120
      TabIndex        =   9
      Text            =   "0"
      Top             =   960
      Width           =   615
   End
   Begin VB.TextBox txtMass 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   2
      Left            =   3120
      TabIndex        =   8
      Text            =   "0"
      Top             =   600
      Width           =   615
   End
   Begin VB.Label Label17 
      Caption         =   "mm"
      Height          =   255
      Left            =   6240
      TabIndex        =   128
      Top             =   3120
      Width           =   255
   End
   Begin VB.Label Label16 
      Caption         =   "mm"
      Height          =   255
      Left            =   3840
      TabIndex        =   127
      Top             =   3120
      Width           =   255
   End
   Begin VB.Label lblLength 
      Alignment       =   1  'Right Justify
      Caption         =   "Length (L3):"
      Height          =   255
      Index           =   3
      Left            =   4440
      TabIndex        =   126
      Top             =   3120
      Width           =   975
   End
   Begin VB.Label lblLength 
      Alignment       =   1  'Right Justify
      Caption         =   "Length (L2):"
      Height          =   255
      Index           =   2
      Left            =   2040
      TabIndex        =   125
      Top             =   3120
      Width           =   975
   End
   Begin VB.Label lblNote2 
      Caption         =   "(2) Each axis Inertia is defined at               its CG parallel to the axis C.S."
      ForeColor       =   &H000040C0&
      Height          =   495
      Left            =   4680
      TabIndex        =   120
      Top             =   7200
      Width           =   3015
   End
   Begin VB.Label lblNote1 
      Caption         =   "(1) Each axis CG is defined in its axis                coordinate system shown in the diagram."
      ForeColor       =   &H000040C0&
      Height          =   855
      Left            =   4680
      TabIndex        =   119
      Top             =   6600
      Width           =   3375
   End
   Begin VB.Label lblNotes 
      Caption         =   "NOTES:"
      ForeColor       =   &H000000FF&
      Height          =   255
      Left            =   4560
      TabIndex        =   118
      Top             =   6240
      Width           =   2895
   End
   Begin VB.Label lblUnits 
      Caption         =   "2"
      BeginProperty Font 
         Name            =   "Comic Sans MS"
         Size            =   6
         Charset         =   0
         Weight          =   400
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   255
      Index           =   24
      Left            =   8880
      TabIndex        =   117
      Top             =   2040
      Width           =   135
   End
   Begin VB.Label lblUnits 
      Caption         =   "kg-m"
      Height          =   255
      Index           =   23
      Left            =   8520
      TabIndex        =   116
      Top             =   2040
      Width           =   495
   End
   Begin VB.Label Label49 
      Caption         =   "2"
      BeginProperty Font 
         Name            =   "Comic Sans MS"
         Size            =   6
         Charset         =   0
         Weight          =   400
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   255
      Left            =   8880
      TabIndex        =   115
      Top             =   2040
      Width           =   135
   End
   Begin VB.Label lblIxx 
      Alignment       =   1  'Right Justify
      Caption         =   "Ixx6:"
      Height          =   255
      Index           =   4
      Left            =   7200
      TabIndex        =   114
      Top             =   2040
      Width           =   495
   End
   Begin VB.Label lblUnits 
      Caption         =   "2"
      BeginProperty Font 
         Name            =   "Comic Sans MS"
         Size            =   6
         Charset         =   0
         Weight          =   400
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   255
      Index           =   32
      Left            =   8880
      TabIndex        =   113
      Top             =   2400
      Width           =   135
   End
   Begin VB.Label lblUnits 
      Caption         =   "kg-m"
      Height          =   255
      Index           =   31
      Left            =   8520
      TabIndex        =   112
      Top             =   2400
      Width           =   495
   End
   Begin VB.Label Label46 
      Caption         =   "2"
      BeginProperty Font 
         Name            =   "Comic Sans MS"
         Size            =   6
         Charset         =   0
         Weight          =   400
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   255
      Left            =   8880
      TabIndex        =   111
      Top             =   2400
      Width           =   135
   End
   Begin VB.Label lblIyy 
      Alignment       =   1  'Right Justify
      Caption         =   "Iyy6:"
      Height          =   255
      Index           =   4
      Left            =   7200
      TabIndex        =   110
      Top             =   2400
      Width           =   495
   End
   Begin VB.Label lblUnits 
      Caption         =   "2"
      BeginProperty Font 
         Name            =   "Comic Sans MS"
         Size            =   6
         Charset         =   0
         Weight          =   400
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   255
      Index           =   22
      Left            =   6600
      TabIndex        =   109
      Top             =   2040
      Width           =   135
   End
   Begin VB.Label lblUnits 
      Caption         =   "kg-m"
      Height          =   255
      Index           =   21
      Left            =   6240
      TabIndex        =   108
      Top             =   2040
      Width           =   495
   End
   Begin VB.Label Label43 
      Caption         =   "2"
      BeginProperty Font 
         Name            =   "Comic Sans MS"
         Size            =   6
         Charset         =   0
         Weight          =   400
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   255
      Left            =   6600
      TabIndex        =   107
      Top             =   2040
      Width           =   135
   End
   Begin VB.Label lblIxx 
      Alignment       =   1  'Right Justify
      Caption         =   "Ixx3:"
      Height          =   255
      Index           =   3
      Left            =   4920
      TabIndex        =   106
      Top             =   2040
      Width           =   495
   End
   Begin VB.Label lblUnits 
      Caption         =   "2"
      BeginProperty Font 
         Name            =   "Comic Sans MS"
         Size            =   6
         Charset         =   0
         Weight          =   400
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   255
      Index           =   30
      Left            =   6600
      TabIndex        =   105
      Top             =   2400
      Width           =   135
   End
   Begin VB.Label lblUnits 
      Caption         =   "kg-m"
      Height          =   255
      Index           =   29
      Left            =   6240
      TabIndex        =   104
      Top             =   2400
      Width           =   495
   End
   Begin VB.Label Label40 
      Caption         =   "2"
      BeginProperty Font 
         Name            =   "Comic Sans MS"
         Size            =   6
         Charset         =   0
         Weight          =   400
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   255
      Left            =   6600
      TabIndex        =   103
      Top             =   2400
      Width           =   135
   End
   Begin VB.Label lblIyy 
      Alignment       =   1  'Right Justify
      Caption         =   "Iyy3:"
      Height          =   255
      Index           =   3
      Left            =   4920
      TabIndex        =   102
      Top             =   2400
      Width           =   495
   End
   Begin VB.Label lblUnits 
      Caption         =   "2"
      BeginProperty Font 
         Name            =   "Comic Sans MS"
         Size            =   6
         Charset         =   0
         Weight          =   400
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   255
      Index           =   20
      Left            =   4200
      TabIndex        =   101
      Top             =   2040
      Width           =   135
   End
   Begin VB.Label lblUnits 
      Caption         =   "kg-m"
      Height          =   255
      Index           =   19
      Left            =   3840
      TabIndex        =   100
      Top             =   2040
      Width           =   495
   End
   Begin VB.Label Label37 
      Caption         =   "2"
      BeginProperty Font 
         Name            =   "Comic Sans MS"
         Size            =   6
         Charset         =   0
         Weight          =   400
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   255
      Left            =   4200
      TabIndex        =   99
      Top             =   2040
      Width           =   135
   End
   Begin VB.Label lblIxx 
      Alignment       =   1  'Right Justify
      Caption         =   "Ixx2:"
      Height          =   255
      Index           =   2
      Left            =   2520
      TabIndex        =   98
      Top             =   2040
      Width           =   495
   End
   Begin VB.Label lblUnits 
      Caption         =   "2"
      BeginProperty Font 
         Name            =   "Comic Sans MS"
         Size            =   6
         Charset         =   0
         Weight          =   400
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   255
      Index           =   28
      Left            =   4200
      TabIndex        =   97
      Top             =   2400
      Width           =   135
   End
   Begin VB.Label lblUnits 
      Caption         =   "kg-m"
      Height          =   255
      Index           =   27
      Left            =   3840
      TabIndex        =   96
      Top             =   2400
      Width           =   495
   End
   Begin VB.Label Label31 
      Caption         =   "2"
      BeginProperty Font 
         Name            =   "Comic Sans MS"
         Size            =   6
         Charset         =   0
         Weight          =   400
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   255
      Left            =   4200
      TabIndex        =   95
      Top             =   2400
      Width           =   135
   End
   Begin VB.Label lblIyy 
      Alignment       =   1  'Right Justify
      Caption         =   "Iyy2:"
      Height          =   255
      Index           =   2
      Left            =   2520
      TabIndex        =   94
      Top             =   2400
      Width           =   495
   End
   Begin VB.Label lblUnits 
      Caption         =   "2"
      BeginProperty Font 
         Name            =   "Comic Sans MS"
         Size            =   6
         Charset         =   0
         Weight          =   400
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   255
      Index           =   18
      Left            =   1800
      TabIndex        =   93
      Top             =   2040
      Width           =   135
   End
   Begin VB.Label lblUnits 
      Caption         =   "kg-m"
      Height          =   255
      Index           =   17
      Left            =   1440
      TabIndex        =   92
      Top             =   2040
      Width           =   495
   End
   Begin VB.Label Label32 
      Caption         =   "2"
      BeginProperty Font 
         Name            =   "Comic Sans MS"
         Size            =   6
         Charset         =   0
         Weight          =   400
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   255
      Left            =   1800
      TabIndex        =   91
      Top             =   2040
      Width           =   135
   End
   Begin VB.Label lblIxx 
      Alignment       =   1  'Right Justify
      Caption         =   "Ixx1:"
      Height          =   255
      Index           =   1
      Left            =   120
      TabIndex        =   90
      Top             =   2040
      Width           =   495
   End
   Begin VB.Label lblUnits 
      Caption         =   "2"
      BeginProperty Font 
         Name            =   "Comic Sans MS"
         Size            =   6
         Charset         =   0
         Weight          =   400
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   255
      Index           =   26
      Left            =   1800
      TabIndex        =   89
      Top             =   2400
      Width           =   135
   End
   Begin VB.Label lblUnits 
      Caption         =   "kg-m"
      Height          =   255
      Index           =   25
      Left            =   1440
      TabIndex        =   88
      Top             =   2400
      Width           =   495
   End
   Begin VB.Label Label28 
      Caption         =   "2"
      BeginProperty Font 
         Name            =   "Comic Sans MS"
         Size            =   6
         Charset         =   0
         Weight          =   400
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   255
      Left            =   1800
      TabIndex        =   87
      Top             =   2400
      Width           =   135
   End
   Begin VB.Label lblIyy 
      Alignment       =   1  'Right Justify
      Caption         =   "Iyy1:"
      Height          =   255
      Index           =   1
      Left            =   120
      TabIndex        =   86
      Top             =   2400
      Width           =   495
   End
   Begin VB.Label lblUnits 
      Caption         =   "mm"
      Height          =   255
      Index           =   16
      Left            =   8520
      TabIndex        =   85
      Top             =   1680
      Width           =   255
   End
   Begin VB.Label lblCGz 
      Alignment       =   1  'Right Justify
      Caption         =   "CGz6:"
      Height          =   255
      Index           =   4
      Left            =   7200
      TabIndex        =   84
      Top             =   1680
      Width           =   495
   End
   Begin VB.Label lblUnits 
      Caption         =   "mm"
      Height          =   255
      Index           =   15
      Left            =   6240
      TabIndex        =   83
      Top             =   1680
      Width           =   255
   End
   Begin VB.Label lblCGz 
      Alignment       =   1  'Right Justify
      Caption         =   "CGz3:"
      Height          =   255
      Index           =   3
      Left            =   4920
      TabIndex        =   82
      Top             =   1680
      Width           =   495
   End
   Begin VB.Label lblUnits 
      Caption         =   "mm"
      Height          =   255
      Index           =   14
      Left            =   3840
      TabIndex        =   81
      Top             =   1680
      Width           =   255
   End
   Begin VB.Label lblCGz 
      Alignment       =   1  'Right Justify
      Caption         =   "CGz2:"
      Height          =   255
      Index           =   2
      Left            =   2520
      TabIndex        =   80
      Top             =   1680
      Width           =   495
   End
   Begin VB.Label lblUnits 
      Caption         =   "mm"
      Height          =   255
      Index           =   13
      Left            =   1440
      TabIndex        =   79
      Top             =   1680
      Width           =   255
   End
   Begin VB.Label lblCGz 
      Alignment       =   1  'Right Justify
      Caption         =   "CGz1:"
      Height          =   255
      Index           =   1
      Left            =   120
      TabIndex        =   78
      Top             =   1680
      Width           =   495
   End
   Begin VB.Label lblUnits 
      Caption         =   "2"
      BeginProperty Font 
         Name            =   "Comic Sans MS"
         Size            =   6
         Charset         =   0
         Weight          =   400
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   255
      Index           =   34
      Left            =   1800
      TabIndex        =   77
      Top             =   2760
      Width           =   135
   End
   Begin VB.Label lblUnits 
      Caption         =   "kg"
      Height          =   255
      Index           =   1
      Left            =   1440
      TabIndex        =   76
      Top             =   600
      Width           =   255
   End
   Begin VB.Label lblUnits 
      Caption         =   "mm"
      Height          =   255
      Index           =   5
      Left            =   1440
      TabIndex        =   75
      Top             =   960
      Width           =   255
   End
   Begin VB.Label lblUnits 
      Caption         =   "mm"
      Height          =   255
      Index           =   9
      Left            =   1440
      TabIndex        =   74
      Top             =   1320
      Width           =   255
   End
   Begin VB.Label lblUnits 
      Caption         =   "kg-m"
      Height          =   255
      Index           =   33
      Left            =   1440
      TabIndex        =   73
      Top             =   2760
      Width           =   495
   End
   Begin VB.Label lblMass 
      Alignment       =   1  'Right Justify
      Caption         =   "Mass:"
      Height          =   255
      Index           =   1
      Left            =   120
      TabIndex        =   72
      Top             =   600
      Width           =   495
   End
   Begin VB.Label lblCGx 
      Alignment       =   1  'Right Justify
      Caption         =   "CGx1:"
      Height          =   255
      Index           =   1
      Left            =   120
      TabIndex        =   71
      Top             =   960
      Width           =   495
   End
   Begin VB.Label lblCGy 
      Alignment       =   1  'Right Justify
      Caption         =   "CGy1:"
      Height          =   255
      Index           =   1
      Left            =   120
      TabIndex        =   70
      Top             =   1320
      Width           =   495
   End
   Begin VB.Label lblIzz 
      Alignment       =   1  'Right Justify
      Caption         =   "Izz1:"
      Height          =   255
      Index           =   1
      Left            =   120
      TabIndex        =   69
      Top             =   2760
      Width           =   495
   End
   Begin VB.Label lblLink1 
      Alignment       =   2  'Center
      Caption         =   "Turret (Axis 1)"
      BeginProperty Font 
         Name            =   "Comic Sans MS"
         Size            =   9.75
         Charset         =   0
         Weight          =   700
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   255
      Left            =   120
      TabIndex        =   62
      Top             =   240
      Width           =   1695
   End
   Begin VB.Label lblNote 
      Caption         =   "NOTE: Diagram shows the user 0 position."
      ForeColor       =   &H00FF0000&
      Height          =   255
      Left            =   360
      TabIndex        =   61
      Top             =   3600
      Width           =   3375
   End
   Begin VB.Label lblUnits 
      Caption         =   "2"
      BeginProperty Font 
         Name            =   "Comic Sans MS"
         Size            =   6
         Charset         =   0
         Weight          =   400
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   255
      Index           =   40
      Left            =   8880
      TabIndex        =   60
      Top             =   2760
      Width           =   135
   End
   Begin VB.Label lblUnits 
      Caption         =   "2"
      BeginProperty Font 
         Name            =   "Comic Sans MS"
         Size            =   6
         Charset         =   0
         Weight          =   400
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   255
      Index           =   38
      Left            =   6600
      TabIndex        =   59
      Top             =   2760
      Width           =   135
   End
   Begin VB.Label lblUnits 
      Caption         =   "kg-m"
      Height          =   255
      Index           =   39
      Left            =   8520
      TabIndex        =   58
      Top             =   2760
      Width           =   375
   End
   Begin VB.Label lblUnits 
      Caption         =   "kg-m"
      Height          =   255
      Index           =   37
      Left            =   6240
      TabIndex        =   57
      Top             =   2760
      Width           =   495
   End
   Begin VB.Label lblUnits 
      Caption         =   "2"
      BeginProperty Font 
         Name            =   "Comic Sans MS"
         Size            =   6
         Charset         =   0
         Weight          =   400
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   255
      Index           =   36
      Left            =   4200
      TabIndex        =   56
      Top             =   2760
      Width           =   135
   End
   Begin VB.Label lblUnits 
      Caption         =   "kg-m"
      Height          =   255
      Index           =   35
      Left            =   3840
      TabIndex        =   55
      Top             =   2760
      Width           =   375
   End
   Begin VB.Label lblUnits 
      Caption         =   "mm"
      Height          =   255
      Index           =   12
      Left            =   8520
      TabIndex        =   54
      Top             =   1320
      Width           =   255
   End
   Begin VB.Label lblUnits 
      Caption         =   "mm"
      Height          =   255
      Index           =   8
      Left            =   8520
      TabIndex        =   53
      Top             =   960
      Width           =   255
   End
   Begin VB.Label lblUnits 
      Caption         =   "mm"
      Height          =   255
      Index           =   11
      Left            =   6240
      TabIndex        =   52
      Top             =   1320
      Width           =   255
   End
   Begin VB.Label lblUnits 
      Caption         =   "mm"
      Height          =   255
      Index           =   7
      Left            =   6240
      TabIndex        =   51
      Top             =   960
      Width           =   255
   End
   Begin VB.Label lblUnits 
      Caption         =   "mm"
      Height          =   255
      Index           =   10
      Left            =   3840
      TabIndex        =   50
      Top             =   1320
      Width           =   255
   End
   Begin VB.Label lblUnits 
      Caption         =   "mm"
      Height          =   255
      Index           =   6
      Left            =   3840
      TabIndex        =   49
      Top             =   960
      Width           =   255
   End
   Begin VB.Label lblUnits 
      Caption         =   "kg"
      Height          =   255
      Index           =   4
      Left            =   8520
      TabIndex        =   48
      Top             =   600
      Width           =   255
   End
   Begin VB.Label lblUnits 
      Caption         =   "kg"
      Height          =   255
      Index           =   3
      Left            =   6240
      TabIndex        =   47
      Top             =   600
      Width           =   255
   End
   Begin VB.Label lblUnits 
      Caption         =   "kg"
      Height          =   255
      Index           =   2
      Left            =   3840
      TabIndex        =   46
      Top             =   600
      Width           =   255
   End
   Begin VB.Label lblPayload 
      Alignment       =   2  'Center
      Caption         =   "Payload"
      BeginProperty Font 
         Name            =   "Comic Sans MS"
         Size            =   9.75
         Charset         =   0
         Weight          =   700
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   255
      Left            =   7560
      TabIndex        =   44
      Top             =   240
      Width           =   1095
   End
   Begin VB.Label lblIzz 
      Alignment       =   1  'Right Justify
      Caption         =   "Izz6:"
      Height          =   255
      Index           =   4
      Left            =   7200
      TabIndex        =   43
      Top             =   2760
      Width           =   495
   End
   Begin VB.Label lblCGy 
      Alignment       =   1  'Right Justify
      Caption         =   "CGy6:"
      Height          =   255
      Index           =   4
      Left            =   7200
      TabIndex        =   42
      Top             =   1320
      Width           =   495
   End
   Begin VB.Label lblCGx 
      Alignment       =   1  'Right Justify
      Caption         =   "CGx6:"
      Height          =   255
      Index           =   4
      Left            =   7200
      TabIndex        =   41
      Top             =   960
      Width           =   495
   End
   Begin VB.Label lblMass 
      Alignment       =   1  'Right Justify
      Caption         =   "Mass:"
      Height          =   255
      Index           =   4
      Left            =   7200
      TabIndex        =   40
      Top             =   600
      Width           =   495
   End
   Begin VB.Label lblIzz 
      Alignment       =   1  'Right Justify
      Caption         =   "Izz3:"
      Height          =   255
      Index           =   3
      Left            =   4920
      TabIndex        =   39
      Top             =   2760
      Width           =   495
   End
   Begin VB.Label lblCGy 
      Alignment       =   1  'Right Justify
      Caption         =   "CGy3:"
      Height          =   255
      Index           =   3
      Left            =   4920
      TabIndex        =   38
      Top             =   1320
      Width           =   495
   End
   Begin VB.Label lblCGx 
      Alignment       =   1  'Right Justify
      Caption         =   "CGx3:"
      Height          =   255
      Index           =   3
      Left            =   4920
      TabIndex        =   37
      Top             =   960
      Width           =   495
   End
   Begin VB.Label lblMass 
      Alignment       =   1  'Right Justify
      Caption         =   "Mass:"
      Height          =   255
      Index           =   3
      Left            =   4920
      TabIndex        =   36
      Top             =   600
      Width           =   495
   End
   Begin VB.Label lblIzz 
      Alignment       =   1  'Right Justify
      Caption         =   "Izz2:"
      Height          =   255
      Index           =   2
      Left            =   2520
      TabIndex        =   35
      Top             =   2760
      Width           =   495
   End
   Begin VB.Label lblCGy 
      Alignment       =   1  'Right Justify
      Caption         =   "CGy2:"
      Height          =   255
      Index           =   2
      Left            =   2520
      TabIndex        =   34
      Top             =   1320
      Width           =   495
   End
   Begin VB.Label lblCGx 
      Alignment       =   1  'Right Justify
      Caption         =   "CGx2:"
      Height          =   255
      Index           =   2
      Left            =   2520
      TabIndex        =   33
      Top             =   960
      Width           =   495
   End
   Begin VB.Label lblMass 
      Alignment       =   1  'Right Justify
      Caption         =   "Mass:"
      Height          =   255
      Index           =   2
      Left            =   2520
      TabIndex        =   32
      Top             =   600
      Width           =   495
   End
   Begin VB.Label lblLink3 
      Caption         =   "Outer Arm (Axis 3)"
      BeginProperty Font 
         Name            =   "Comic Sans MS"
         Size            =   9.75
         Charset         =   0
         Weight          =   700
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   255
      Left            =   4800
      TabIndex        =   30
      Top             =   240
      Width           =   1935
   End
   Begin VB.Label lblLink2 
      Caption         =   "Inner Arm (Axis 2)"
      BeginProperty Font 
         Name            =   "Comic Sans MS"
         Size            =   9.75
         Charset         =   0
         Weight          =   700
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   255
      Left            =   2400
      TabIndex        =   0
      Top             =   240
      Width           =   1935
   End
End
Attribute VB_Name = "frmDynamics"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False

Private Sub cmdCancel_Click()
    
    'Unload Me
    Hide
    frmInput.Show
End Sub

Private Sub cmdDynamics_Click()

    frmDynamics.Hide
    Screen.MousePointer = vbHourglass
    frmInput.Show
    Call P50_Main.P50_Main
End Sub


Private Sub cmdRestore_Click()
Dim I As Integer

For I = 1 To 4
    txtMass(I).Text = "0"
    txtCGx(I).Text = "0"
    txtCGy(I).Text = "0"
    txtCGz(I).Text = "0"
    txtIxx(I).Text = "0"
    txtIyy(I).Text = "0"
    txtIzz(I).Text = "0"
Next I

txtLength(2).Text = "500"
txtLength(3).Text = "500"

End Sub


Private Sub Form_Load()
Dim I As Integer

'Temporarily hide the input form
frmInput.Hide

For I = 1 To 40
    lblUnits(I).Enabled = False
Next I

For I = 1 To 4
    lblMass(I).Enabled = False
    lblCGx(I).Enabled = False
    lblCGy(I).Enabled = False
    lblCGz(I).Enabled = False
    lblIxx(I).Enabled = False
    lblIyy(I).Enabled = False
    lblIzz(I).Enabled = False

    txtMass(I).Enabled = False
    txtCGx(I).Enabled = False
    txtCGy(I).Enabled = False
    txtCGz(I).Enabled = False
    txtIxx(I).Enabled = False
    txtIyy(I).Enabled = False
    txtIzz(I).Enabled = False
Next I

lblNotes.Enabled = False
lblNote1.Enabled = False
lblNote2.Enabled = False

chkWantTorque.Enabled = False
chkWantForce.Enabled = False
chkGrounded.Enabled = False
End Sub

Private Sub optDynamics_Click()
Dim I As Integer

For I = 1 To 40
    lblUnits(I).Enabled = True
Next I

For I = 1 To 4
    lblMass(I).Enabled = True
    lblCGx(I).Enabled = True
    lblCGy(I).Enabled = True
    lblCGz(I).Enabled = True
    lblIxx(I).Enabled = True
    lblIyy(I).Enabled = True
    lblIzz(I).Enabled = True

    txtMass(I).Enabled = True
    txtCGx(I).Enabled = True
    txtCGy(I).Enabled = True
    txtCGz(I).Enabled = True
    txtIxx(I).Enabled = True
    txtIyy(I).Enabled = True
    txtIzz(I).Enabled = True
Next I

lblNotes.Enabled = True
lblNote1.Enabled = True
lblNote2.Enabled = True

chkWantTorque.Enabled = True
chkWantForce.Enabled = True
chkGrounded.Enabled = True

chkWantTorque.Value = Checked
chkWantForce.Value = Unchecked
chkGrounded.Value = Unchecked


End Sub

Private Sub optMotion_Click()
Dim I As Integer

For I = 1 To 40
    lblUnits(I).Enabled = False
Next I

For I = 1 To 4
    lblMass(I).Enabled = False
    lblCGx(I).Enabled = False
    lblCGy(I).Enabled = False
    lblCGz(I).Enabled = False
    lblIxx(I).Enabled = False
    lblIyy(I).Enabled = False
    lblIzz(I).Enabled = False

    txtMass(I).Enabled = False
    txtCGx(I).Enabled = False
    txtCGy(I).Enabled = False
    txtCGz(I).Enabled = False
    txtIxx(I).Enabled = False
    txtIyy(I).Enabled = False
    txtIzz(I).Enabled = False
Next I

lblNotes.Enabled = False
lblNote1.Enabled = False
lblNote2.Enabled = False

chkWantTorque.Enabled = False
chkWantForce.Enabled = False
chkGrounded.Enabled = False


End Sub

Private Sub txtCGy_GotFocus(Index As Integer)
    txtCGy(Index).SelStart = 0
    ' Highlight to end of text.
    txtCGy(Index).SelLength = Len(txtCGy(Index).Text)
End Sub

Private Sub txtCGy_KeyPress(Index As Integer, KeyAscii As Integer)
    If KeyAscii <> 8 And KeyAscii <> 45 And KeyAscii <> 46 Then
        If Chr(KeyAscii) < "0" Or Chr(KeyAscii) > "9" Then
            KeyAscii = 0
            Beep
        End If
    End If
End Sub

Private Sub txtCGx_GotFocus(Index As Integer)
    txtCGx(Index).SelStart = 0
    ' Highlight to end of text.
    txtCGx(Index).SelLength = Len(txtCGx(Index).Text)
End Sub

Private Sub txtCGx_KeyPress(Index As Integer, KeyAscii As Integer)
    If KeyAscii <> 8 And KeyAscii <> 45 And KeyAscii <> 46 Then
        If Chr(KeyAscii) < "0" Or Chr(KeyAscii) > "9" Then
            KeyAscii = 0
            Beep
        End If
    End If
End Sub

Private Sub txtCGz_GotFocus(Index As Integer)
    txtCGz(Index).SelStart = 0
    ' Highlight to end of text.
    txtCGz(Index).SelLength = Len(txtCGz(Index).Text)
End Sub

Private Sub txtCGz_KeyPress(Index As Integer, KeyAscii As Integer)
    If KeyAscii <> 8 And KeyAscii <> 45 And KeyAscii <> 46 Then
        If Chr(KeyAscii) < "0" Or Chr(KeyAscii) > "9" Then
            KeyAscii = 0
            Beep
        End If
    End If
End Sub

Private Sub txtIxx_KeyPress(Index As Integer, KeyAscii As Integer)
    If KeyAscii <> 8 And KeyAscii <> 45 And KeyAscii <> 46 Then
        If Chr(KeyAscii) < "0" Or Chr(KeyAscii) > "9" Then
            KeyAscii = 0
            Beep
        End If
    End If
End Sub

Private Sub txtIxx_GotFocus(Index As Integer)
    txtIxx(Index).SelStart = 0
    ' Highlight to end of text.
    txtIxx(Index).SelLength = Len(txtIxx(Index).Text)
End Sub

Private Sub txtIxx_LostFocus(Index As Integer)
    If Val(txtIxx(Index).Text) < 0 Then
        MsgBox "Inertia must be greater than or equal to 0!", _
        vbExclamation, "Inertia Error"
        txtIxx(Index).SetFocus
    End If
End Sub

Private Sub txtIyy_GotFocus(Index As Integer)
    txtIyy(Index).SelStart = 0
    ' Highlight to end of text.
    txtIyy(Index).SelLength = Len(txtIyy(Index).Text)
End Sub

Private Sub txtIyy_KeyPress(Index As Integer, KeyAscii As Integer)
    If KeyAscii <> 8 And KeyAscii <> 45 And KeyAscii <> 46 Then
        If Chr(KeyAscii) < "0" Or Chr(KeyAscii) > "9" Then
            KeyAscii = 0
            Beep
        End If
    End If
End Sub

Private Sub txtIyy_LostFocus(Index As Integer)
    If Val(txtIyy(Index).Text) < 0 Then
        MsgBox "Inertia must be greater than or equal to 0!", _
        vbExclamation, "Inertia Error"
        txtIyy(Index).SetFocus
    End If
End Sub

Private Sub txtIzz_GotFocus(Index As Integer)
    txtIzz(Index).SelStart = 0
    ' Highlight to end of text.
    txtIzz(Index).SelLength = Len(txtIzz(Index).Text)
End Sub

Private Sub txtIzz_KeyPress(Index As Integer, KeyAscii As Integer)
    If KeyAscii <> 8 And KeyAscii <> 45 And KeyAscii <> 46 Then
        If Chr(KeyAscii) < "0" Or Chr(KeyAscii) > "9" Then
            KeyAscii = 0
            Beep
        End If
    End If
End Sub

Private Sub txtIzz_LostFocus(Index As Integer)
    If Val(txtIzz(Index).Text) < 0 Then
        MsgBox "Inertia must be greater than or equal to 0!", _
        vbExclamation, "Inertia Error"
        txtIzz(Index).SetFocus
    End If
End Sub

Private Sub txtMass_GotFocus(Index As Integer)
    txtMass(Index).SelStart = 0
    ' Highlight to end of text.
    txtMass(Index).SelLength = Len(txtMass(Index).Text)
End Sub

Private Sub txtMass_LostFocus(Index As Integer)
    If Val(txtMass(Index).Text) < 0 Then
        MsgBox "Mass must be greater than or equal to 0!", _
        vbExclamation, "Mass Error"
        txtMass(Index).SetFocus
    End If
End Sub

Private Sub txtLength_GotFocus(Index As Integer)
    txtLength(Index).SelStart = 0
    ' Highlight to end of text.
    txtLength(Index).SelLength = Len(txtLength(Index).Text)
End Sub

Private Sub txtLength_LostFocus(Index As Integer)
    If Val(txtLength(Index).Text) <= 0 Then
        MsgBox "Arm Length must be greater than 0!", _
        vbExclamation, "Arm Length Error"
        txtLength(Index).SetFocus
    End If
End Sub

