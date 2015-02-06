VERSION 5.00
Begin VB.Form frmInput 
   BackColor       =   &H8000000A&
   BorderStyle     =   1  'Fixed Single
   Caption         =   "P-50 Motion Input"
   ClientHeight    =   7215
   ClientLeft      =   3120
   ClientTop       =   2580
   ClientWidth     =   10185
   BeginProperty Font 
      Name            =   "Comic Sans MS"
      Size            =   8.25
      Charset         =   0
      Weight          =   400
      Underline       =   0   'False
      Italic          =   0   'False
      Strikethrough   =   0   'False
   EndProperty
   Icon            =   "frmP50InputMUDS.frx":0000
   LinkTopic       =   "Form1"
   MaxButton       =   0   'False
   MousePointer    =   1  'Arrow
   ScaleHeight     =   7215
   ScaleWidth      =   10185
   Begin VB.TextBox txtJNTSpeed 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   2
      Left            =   6360
      TabIndex        =   131
      Text            =   "90"
      Top             =   5280
      Width           =   735
   End
   Begin VB.TextBox txtJNTSpeed 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   3
      Left            =   7200
      TabIndex        =   130
      Text            =   "90"
      Top             =   5280
      Width           =   735
   End
   Begin VB.TextBox txtJNTSpeed 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   1
      Left            =   5520
      TabIndex        =   129
      Text            =   "90"
      Top             =   5280
      Width           =   735
   End
   Begin VB.TextBox txtJNTAcc2 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   3
      Left            =   7200
      MousePointer    =   3  'I-Beam
      TabIndex        =   57
      Text            =   "200"
      Top             =   4560
      Width           =   735
   End
   Begin VB.TextBox txtJNTAcc2 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   2
      Left            =   6360
      MousePointer    =   3  'I-Beam
      TabIndex        =   56
      Text            =   "200"
      Top             =   4560
      Width           =   735
   End
   Begin VB.TextBox txtJNTAcc1 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   3
      Left            =   7200
      MousePointer    =   3  'I-Beam
      TabIndex        =   54
      Text            =   "400"
      Top             =   4200
      Width           =   735
   End
   Begin VB.TextBox txtJNTAcc1 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   2
      Left            =   6360
      MousePointer    =   3  'I-Beam
      TabIndex        =   53
      Text            =   "400"
      Top             =   4200
      Width           =   735
   End
   Begin VB.Frame frameNumPoints 
      Caption         =   "Number of Taught Points"
      Height          =   1215
      Left            =   120
      TabIndex        =   111
      Top             =   1680
      Width           =   3735
      Begin VB.OptionButton optNumPoints 
         Caption         =   "10 Points"
         BeginProperty Font 
            Name            =   "MS Sans Serif"
            Size            =   8.25
            Charset         =   0
            Weight          =   400
            Underline       =   0   'False
            Italic          =   0   'False
            Strikethrough   =   0   'False
         EndProperty
         Height          =   255
         Index           =   10
         Left            =   2520
         TabIndex        =   120
         TabStop         =   0   'False
         Top             =   840
         Width           =   975
      End
      Begin VB.OptionButton optNumPoints 
         Caption         =   "9 Points"
         BeginProperty Font 
            Name            =   "MS Sans Serif"
            Size            =   8.25
            Charset         =   0
            Weight          =   400
            Underline       =   0   'False
            Italic          =   0   'False
            Strikethrough   =   0   'False
         EndProperty
         Height          =   255
         Index           =   9
         Left            =   2520
         TabIndex        =   119
         TabStop         =   0   'False
         Top             =   600
         Width           =   975
      End
      Begin VB.OptionButton optNumPoints 
         Caption         =   "8 Points"
         BeginProperty Font 
            Name            =   "MS Sans Serif"
            Size            =   8.25
            Charset         =   0
            Weight          =   400
            Underline       =   0   'False
            Italic          =   0   'False
            Strikethrough   =   0   'False
         EndProperty
         Height          =   255
         Index           =   8
         Left            =   2520
         TabIndex        =   118
         TabStop         =   0   'False
         Top             =   360
         Width           =   975
      End
      Begin VB.OptionButton optNumPoints 
         Caption         =   "7 Points"
         BeginProperty Font 
            Name            =   "MS Sans Serif"
            Size            =   8.25
            Charset         =   0
            Weight          =   400
            Underline       =   0   'False
            Italic          =   0   'False
            Strikethrough   =   0   'False
         EndProperty
         Height          =   255
         Index           =   7
         Left            =   1320
         TabIndex        =   117
         TabStop         =   0   'False
         Top             =   840
         Width           =   975
      End
      Begin VB.OptionButton optNumPoints 
         Caption         =   "6 Points"
         BeginProperty Font 
            Name            =   "MS Sans Serif"
            Size            =   8.25
            Charset         =   0
            Weight          =   400
            Underline       =   0   'False
            Italic          =   0   'False
            Strikethrough   =   0   'False
         EndProperty
         Height          =   255
         Index           =   6
         Left            =   1320
         TabIndex        =   116
         TabStop         =   0   'False
         Top             =   600
         Width           =   975
      End
      Begin VB.OptionButton optNumPoints 
         Caption         =   "5 Points"
         BeginProperty Font 
            Name            =   "MS Sans Serif"
            Size            =   8.25
            Charset         =   0
            Weight          =   400
            Underline       =   0   'False
            Italic          =   0   'False
            Strikethrough   =   0   'False
         EndProperty
         Height          =   255
         Index           =   5
         Left            =   1320
         TabIndex        =   115
         TabStop         =   0   'False
         Top             =   360
         Width           =   975
      End
      Begin VB.OptionButton optNumPoints 
         Caption         =   "4 Points"
         BeginProperty Font 
            Name            =   "MS Sans Serif"
            Size            =   8.25
            Charset         =   0
            Weight          =   400
            Underline       =   0   'False
            Italic          =   0   'False
            Strikethrough   =   0   'False
         EndProperty
         Height          =   255
         Index           =   4
         Left            =   240
         TabIndex        =   114
         TabStop         =   0   'False
         Top             =   840
         Width           =   975
      End
      Begin VB.OptionButton optNumPoints 
         Caption         =   "3 Points"
         BeginProperty Font 
            Name            =   "MS Sans Serif"
            Size            =   8.25
            Charset         =   0
            Weight          =   400
            Underline       =   0   'False
            Italic          =   0   'False
            Strikethrough   =   0   'False
         EndProperty
         Height          =   255
         Index           =   3
         Left            =   240
         TabIndex        =   113
         TabStop         =   0   'False
         Top             =   600
         Width           =   975
      End
      Begin VB.OptionButton optNumPoints 
         Caption         =   "2 Points"
         BeginProperty Font 
            Name            =   "MS Sans Serif"
            Size            =   8.25
            Charset         =   0
            Weight          =   400
            Underline       =   0   'False
            Italic          =   0   'False
            Strikethrough   =   0   'False
         EndProperty
         Height          =   255
         Index           =   2
         Left            =   240
         TabIndex        =   112
         TabStop         =   0   'False
         Top             =   360
         Value           =   -1  'True
         Width           =   975
      End
   End
   Begin VB.Frame frameCart 
      Caption         =   "Linear Accel Times"
      Height          =   1455
      Left            =   1920
      TabIndex        =   106
      Top             =   120
      Width           =   2775
      Begin VB.TextBox txtCartAcc1 
         Alignment       =   1  'Right Justify
         Height          =   285
         Left            =   1200
         MousePointer    =   3  'I-Beam
         TabIndex        =   2
         Text            =   "400"
         Top             =   360
         Width           =   615
      End
      Begin VB.TextBox txtCartAcc2 
         Alignment       =   1  'Right Justify
         Height          =   285
         Left            =   1200
         MousePointer    =   3  'I-Beam
         TabIndex        =   3
         Text            =   "200"
         Top             =   720
         Width           =   615
      End
      Begin VB.Label lblCartAccUnit 
         Caption         =   "millisec."
         Height          =   285
         Index           =   2
         Left            =   1920
         TabIndex        =   128
         Top             =   1080
         Width           =   705
      End
      Begin VB.Label lblCartAccUnit 
         Caption         =   "millisec."
         Height          =   285
         Index           =   1
         Left            =   1920
         TabIndex        =   127
         Top             =   720
         Width           =   705
      End
      Begin VB.Label lblCartAccUnit 
         Caption         =   "millisec."
         Height          =   285
         Index           =   0
         Left            =   1920
         TabIndex        =   126
         Top             =   360
         Width           =   705
      End
      Begin VB.Label lblCartAccTotal 
         Alignment       =   1  'Right Justify
         Caption         =   "Total Accel:"
         Height          =   240
         Left            =   120
         TabIndex        =   110
         Top             =   1080
         Width           =   960
      End
      Begin VB.Label lblCartAcc2 
         Alignment       =   1  'Right Justify
         Caption         =   "CartAccel 2:"
         Height          =   285
         Left            =   120
         TabIndex        =   109
         ToolTipText     =   "Cartesian Acceleration Time 2"
         Top             =   720
         Width           =   960
      End
      Begin VB.Label txtCartAccTotal 
         Alignment       =   1  'Right Justify
         BorderStyle     =   1  'Fixed Single
         Caption         =   "600"
         Height          =   285
         Left            =   1200
         TabIndex        =   108
         Top             =   1080
         Width           =   615
      End
      Begin VB.Label lblCartAcc1 
         Alignment       =   1  'Right Justify
         Caption         =   "CartAccel 1:"
         Height          =   285
         Left            =   120
         TabIndex        =   107
         ToolTipText     =   "Cartesian Acceleration Time 1"
         Top             =   360
         Width           =   960
      End
   End
   Begin VB.TextBox txtEnd 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   7
      Left            =   9600
      MousePointer    =   3  'I-Beam
      TabIndex        =   42
      Text            =   "0"
      Top             =   3120
      Width           =   495
   End
   Begin VB.TextBox txtEnd 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   6
      Left            =   9600
      MousePointer    =   3  'I-Beam
      TabIndex        =   37
      Text            =   "0"
      Top             =   2760
      Width           =   495
   End
   Begin VB.TextBox txtEnd 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   5
      Left            =   9600
      MousePointer    =   3  'I-Beam
      TabIndex        =   32
      Text            =   "0"
      Top             =   2400
      Width           =   495
   End
   Begin VB.TextBox txtEnd 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   4
      Left            =   9600
      MousePointer    =   3  'I-Beam
      TabIndex        =   27
      Text            =   "0"
      Top             =   2040
      Width           =   495
   End
   Begin VB.TextBox txtEnd 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   3
      Left            =   9600
      MousePointer    =   3  'I-Beam
      TabIndex        =   22
      Text            =   "0"
      Top             =   1680
      Width           =   495
   End
   Begin VB.TextBox txtEnd 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   2
      Left            =   9600
      MousePointer    =   3  'I-Beam
      TabIndex        =   17
      Text            =   "0"
      Top             =   1320
      Width           =   495
   End
   Begin VB.TextBox txtEnd 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   1
      Left            =   9600
      MousePointer    =   3  'I-Beam
      TabIndex        =   12
      Text            =   "0"
      Top             =   960
      Width           =   495
   End
   Begin VB.TextBox txtEnd 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   8
      Left            =   9600
      MousePointer    =   3  'I-Beam
      TabIndex        =   47
      Text            =   "0"
      Top             =   3480
      Width           =   495
   End
   Begin VB.TextBox txtSpeed 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   9
      Left            =   8040
      MousePointer    =   3  'I-Beam
      TabIndex        =   51
      Text            =   "1500"
      Top             =   3840
      Width           =   645
   End
   Begin VB.TextBox txtPz 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   9
      Left            =   7200
      MousePointer    =   3  'I-Beam
      TabIndex        =   50
      Text            =   "0"
      Top             =   3840
      Width           =   735
   End
   Begin VB.TextBox txtPy 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   9
      Left            =   6360
      MousePointer    =   3  'I-Beam
      TabIndex        =   49
      Text            =   "0"
      Top             =   3840
      Width           =   735
   End
   Begin VB.TextBox txtPx 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   9
      Left            =   5520
      MousePointer    =   3  'I-Beam
      TabIndex        =   48
      Text            =   "0"
      Top             =   3840
      Width           =   735
   End
   Begin VB.TextBox txtSpeed 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   8
      Left            =   8040
      MousePointer    =   3  'I-Beam
      TabIndex        =   46
      Text            =   "1500"
      Top             =   3480
      Width           =   645
   End
   Begin VB.TextBox txtPz 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   8
      Left            =   7200
      MousePointer    =   3  'I-Beam
      TabIndex        =   45
      Text            =   "0"
      Top             =   3480
      Width           =   735
   End
   Begin VB.TextBox txtPy 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   8
      Left            =   6360
      MousePointer    =   3  'I-Beam
      TabIndex        =   44
      Text            =   "0"
      Top             =   3480
      Width           =   735
   End
   Begin VB.TextBox txtPx 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   8
      Left            =   5520
      MousePointer    =   3  'I-Beam
      TabIndex        =   43
      Text            =   "0"
      Top             =   3480
      Width           =   735
   End
   Begin VB.TextBox txtSpeed 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   7
      Left            =   8040
      MousePointer    =   3  'I-Beam
      TabIndex        =   41
      Text            =   "1500"
      Top             =   3120
      Width           =   645
   End
   Begin VB.TextBox txtPz 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   7
      Left            =   7200
      MousePointer    =   3  'I-Beam
      TabIndex        =   40
      Text            =   "0"
      Top             =   3120
      Width           =   735
   End
   Begin VB.TextBox txtPy 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   7
      Left            =   6360
      MousePointer    =   3  'I-Beam
      TabIndex        =   39
      Text            =   "0"
      Top             =   3120
      Width           =   735
   End
   Begin VB.TextBox txtPx 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   7
      Left            =   5520
      MousePointer    =   3  'I-Beam
      TabIndex        =   38
      Text            =   "0"
      Top             =   3120
      Width           =   735
   End
   Begin VB.TextBox txtSpeed 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   6
      Left            =   8040
      MousePointer    =   3  'I-Beam
      TabIndex        =   36
      Text            =   "1500"
      Top             =   2760
      Width           =   645
   End
   Begin VB.TextBox txtPz 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   6
      Left            =   7200
      MousePointer    =   3  'I-Beam
      TabIndex        =   35
      Text            =   "0"
      Top             =   2760
      Width           =   735
   End
   Begin VB.TextBox txtPy 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   6
      Left            =   6360
      MousePointer    =   3  'I-Beam
      TabIndex        =   34
      Text            =   "0"
      Top             =   2760
      Width           =   735
   End
   Begin VB.TextBox txtPx 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   6
      Left            =   5520
      MousePointer    =   3  'I-Beam
      TabIndex        =   33
      Text            =   "0"
      Top             =   2760
      Width           =   735
   End
   Begin VB.TextBox txtSpeed 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   5
      Left            =   8040
      MousePointer    =   3  'I-Beam
      TabIndex        =   31
      Text            =   "1500"
      Top             =   2400
      Width           =   645
   End
   Begin VB.TextBox txtPz 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   5
      Left            =   7200
      MousePointer    =   3  'I-Beam
      TabIndex        =   30
      Text            =   "0"
      Top             =   2400
      Width           =   735
   End
   Begin VB.TextBox txtPy 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   5
      Left            =   6360
      MousePointer    =   3  'I-Beam
      TabIndex        =   29
      Text            =   "0"
      Top             =   2400
      Width           =   735
   End
   Begin VB.TextBox txtPx 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   5
      Left            =   5520
      MousePointer    =   3  'I-Beam
      TabIndex        =   28
      Text            =   "0"
      Top             =   2400
      Width           =   735
   End
   Begin VB.TextBox txtSpeed 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   4
      Left            =   8040
      MousePointer    =   3  'I-Beam
      TabIndex        =   26
      Text            =   "1500"
      Top             =   2040
      Width           =   645
   End
   Begin VB.TextBox txtSpeed 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   3
      Left            =   8040
      MousePointer    =   3  'I-Beam
      TabIndex        =   21
      Text            =   "1500"
      Top             =   1680
      Width           =   645
   End
   Begin VB.TextBox txtSpeed 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   2
      Left            =   8040
      MousePointer    =   3  'I-Beam
      TabIndex        =   16
      Text            =   "1500"
      Top             =   1320
      Width           =   645
   End
   Begin VB.TextBox txtPz 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   4
      Left            =   7200
      MousePointer    =   3  'I-Beam
      TabIndex        =   25
      Text            =   "0"
      Top             =   2040
      Width           =   735
   End
   Begin VB.TextBox txtPz 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   3
      Left            =   7200
      MousePointer    =   3  'I-Beam
      TabIndex        =   20
      Text            =   "0"
      Top             =   1680
      Width           =   735
   End
   Begin VB.TextBox txtPz 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   2
      Left            =   7200
      MousePointer    =   3  'I-Beam
      TabIndex        =   15
      Text            =   "0"
      Top             =   1320
      Width           =   735
   End
   Begin VB.TextBox txtPz 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   1
      Left            =   7200
      MousePointer    =   3  'I-Beam
      TabIndex        =   10
      Text            =   "0"
      Top             =   960
      Width           =   735
   End
   Begin VB.TextBox txtPz 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   0
      Left            =   7200
      MousePointer    =   3  'I-Beam
      TabIndex        =   7
      Text            =   "0"
      Top             =   600
      Width           =   735
   End
   Begin VB.TextBox txtPy 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   4
      Left            =   6360
      MousePointer    =   3  'I-Beam
      TabIndex        =   24
      Text            =   "0"
      Top             =   2040
      Width           =   735
   End
   Begin VB.TextBox txtPy 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   3
      Left            =   6360
      MousePointer    =   3  'I-Beam
      TabIndex        =   19
      Text            =   "0"
      Top             =   1680
      Width           =   735
   End
   Begin VB.TextBox txtPy 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   2
      Left            =   6360
      MousePointer    =   3  'I-Beam
      TabIndex        =   14
      Text            =   "0"
      Top             =   1320
      Width           =   735
   End
   Begin VB.TextBox txtPy 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   1
      Left            =   6360
      MousePointer    =   3  'I-Beam
      TabIndex        =   9
      Text            =   "0"
      Top             =   960
      Width           =   735
   End
   Begin VB.TextBox txtPy 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   0
      Left            =   6360
      MousePointer    =   3  'I-Beam
      TabIndex        =   6
      Text            =   "0"
      Top             =   600
      Width           =   735
   End
   Begin VB.TextBox txtPx 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   4
      Left            =   5520
      MousePointer    =   3  'I-Beam
      TabIndex        =   23
      Text            =   "0"
      Top             =   2040
      Width           =   735
   End
   Begin VB.TextBox txtPx 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   3
      Left            =   5520
      MousePointer    =   3  'I-Beam
      TabIndex        =   18
      Text            =   "0"
      Top             =   1680
      Width           =   735
   End
   Begin VB.TextBox txtPx 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   2
      Left            =   5520
      MousePointer    =   3  'I-Beam
      TabIndex        =   13
      Text            =   "0"
      Top             =   1320
      Width           =   735
   End
   Begin VB.TextBox txtPx 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   1
      Left            =   5520
      MousePointer    =   3  'I-Beam
      TabIndex        =   8
      Text            =   "0"
      Top             =   960
      Width           =   735
   End
   Begin VB.TextBox txtPx 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   0
      Left            =   5520
      MousePointer    =   3  'I-Beam
      TabIndex        =   5
      Text            =   "0"
      Top             =   600
      Width           =   735
   End
   Begin VB.CommandButton cmdStatus 
      Caption         =   "Check Path S&tatus"
      Height          =   495
      Left            =   120
      TabIndex        =   59
      Top             =   6600
      Width           =   1695
   End
   Begin VB.TextBox txtOutput 
      Alignment       =   1  'Right Justify
      BeginProperty Font 
         Name            =   "MS Sans Serif"
         Size            =   8.25
         Charset         =   0
         Weight          =   400
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   240
      Left            =   1680
      MousePointer    =   3  'I-Beam
      TabIndex        =   58
      Text            =   "4"
      Top             =   5220
      Width           =   510
   End
   Begin VB.CommandButton cmdExit 
      Caption         =   "E&xit Program"
      Height          =   495
      Left            =   3720
      TabIndex        =   61
      Top             =   6600
      Width           =   1695
   End
   Begin VB.CommandButton cmdCalculate 
      Caption         =   "&Calculate Motion"
      Height          =   510
      Left            =   1920
      TabIndex        =   60
      Top             =   6600
      Width           =   1695
   End
   Begin VB.TextBox txtUpdate 
      Alignment       =   1  'Right Justify
      Height          =   285
      Left            =   2040
      MousePointer    =   3  'I-Beam
      TabIndex        =   4
      Text            =   "8"
      Top             =   3120
      Width           =   525
   End
   Begin VB.TextBox txtJNTAcc2 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   1
      Left            =   5520
      MousePointer    =   3  'I-Beam
      TabIndex        =   55
      Text            =   "200"
      Top             =   4560
      Width           =   735
   End
   Begin VB.TextBox txtJNTAcc1 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   1
      Left            =   5520
      MousePointer    =   3  'I-Beam
      TabIndex        =   52
      Text            =   "400"
      Top             =   4200
      Width           =   735
   End
   Begin VB.TextBox txtSpeed 
      Alignment       =   1  'Right Justify
      Height          =   285
      Index           =   1
      Left            =   8040
      MousePointer    =   3  'I-Beam
      TabIndex        =   11
      Text            =   "1500"
      Top             =   960
      Width           =   645
   End
   Begin VB.Frame frameType 
      Caption         =   "Motion Type"
      Height          =   1140
      Left            =   120
      TabIndex        =   1
      Top             =   120
      Width           =   1695
      Begin VB.OptionButton optRotate 
         Caption         =   "Joint Motion"
         Height          =   285
         Left            =   120
         TabIndex        =   62
         TabStop         =   0   'False
         Top             =   720
         Width           =   1320
      End
      Begin VB.OptionButton optLinear 
         Caption         =   "Linear Motion"
         Height          =   285
         Left            =   120
         TabIndex        =   0
         TabStop         =   0   'False
         Top             =   360
         Value           =   -1  'True
         Width           =   1455
      End
   End
   Begin VB.Frame frameServo 
      Caption         =   "Servo Parameters"
      Height          =   1395
      Left            =   120
      TabIndex        =   67
      Top             =   3600
      Width           =   3765
      Begin VB.TextBox txtExp 
         Alignment       =   1  'Right Justify
         BeginProperty Font 
            Name            =   "MS Sans Serif"
            Size            =   8.25
            Charset         =   0
            Weight          =   400
            Underline       =   0   'False
            Italic          =   0   'False
            Strikethrough   =   0   'False
         EndProperty
         Height          =   330
         Left            =   2040
         MousePointer    =   3  'I-Beam
         TabIndex        =   71
         TabStop         =   0   'False
         Text            =   "0"
         Top             =   960
         Width           =   480
      End
      Begin VB.OptionButton optDblLinear 
         Caption         =   "Double Linear Servo Filter"
         Height          =   375
         Left            =   195
         TabIndex        =   69
         TabStop         =   0   'False
         Top             =   240
         Value           =   -1  'True
         Width           =   2340
      End
      Begin VB.OptionButton optExp 
         Caption         =   "Exponential Servo Filter"
         Height          =   375
         Left            =   195
         TabIndex        =   68
         TabStop         =   0   'False
         Top             =   600
         Width           =   2295
      End
      Begin VB.Label lblUnitsExp 
         Caption         =   "milliseconds"
         Height          =   285
         Left            =   2640
         TabIndex        =   72
         Top             =   960
         Width           =   915
      End
      Begin VB.Label lblExp 
         Caption         =   "Exponential Decay Time:"
         Height          =   240
         Left            =   480
         TabIndex        =   70
         Top             =   960
         Width           =   1455
      End
   End
   Begin VB.Label lblPercent 
      Caption         =   "%"
      Height          =   255
      Index           =   8
      Left            =   8760
      TabIndex        =   142
      Top             =   3480
      Visible         =   0   'False
      Width           =   255
   End
   Begin VB.Label lblPercent 
      Caption         =   "%"
      Height          =   255
      Index           =   7
      Left            =   8760
      TabIndex        =   141
      Top             =   3120
      Visible         =   0   'False
      Width           =   255
   End
   Begin VB.Label lblPercent 
      Caption         =   "%"
      Height          =   255
      Index           =   6
      Left            =   8760
      TabIndex        =   140
      Top             =   2760
      Visible         =   0   'False
      Width           =   255
   End
   Begin VB.Label lblPercent 
      Caption         =   "%"
      Height          =   255
      Index           =   5
      Left            =   8760
      TabIndex        =   139
      Top             =   2400
      Visible         =   0   'False
      Width           =   255
   End
   Begin VB.Label lblPercent 
      Caption         =   "%"
      Height          =   255
      Index           =   4
      Left            =   8760
      TabIndex        =   138
      Top             =   2040
      Visible         =   0   'False
      Width           =   255
   End
   Begin VB.Label lblPercent 
      Caption         =   "%"
      Height          =   255
      Index           =   3
      Left            =   8760
      TabIndex        =   137
      Top             =   1680
      Visible         =   0   'False
      Width           =   255
   End
   Begin VB.Label lblPercent 
      Caption         =   "%"
      Height          =   255
      Index           =   2
      Left            =   8760
      TabIndex        =   136
      Top             =   1320
      Visible         =   0   'False
      Width           =   255
   End
   Begin VB.Label lblPercent 
      Caption         =   "%"
      Height          =   255
      Index           =   1
      Left            =   8760
      TabIndex        =   135
      Top             =   960
      Visible         =   0   'False
      Width           =   255
   End
   Begin VB.Label lblPercent 
      Caption         =   "%"
      Height          =   255
      Index           =   9
      Left            =   8760
      TabIndex        =   134
      Top             =   3840
      Visible         =   0   'False
      Width           =   255
   End
   Begin VB.Label lblJNTSpeedUnits 
      Caption         =   "°/sec"
      Height          =   255
      Left            =   8040
      TabIndex        =   133
      Top             =   5280
      Width           =   495
   End
   Begin VB.Label lblJNTSpeed 
      Alignment       =   1  'Right Justify
      Caption         =   "Joint Speed"
      Height          =   285
      Left            =   4440
      TabIndex        =   132
      Top             =   5280
      Width           =   975
   End
   Begin VB.Label lblJNTaccUnit 
      Caption         =   "milliseconds."
      Height          =   330
      Index           =   2
      Left            =   8040
      TabIndex        =   125
      Top             =   4920
      Width           =   960
   End
   Begin VB.Label lblJNTaccUnit 
      Caption         =   "milliseconds."
      Height          =   330
      Index           =   1
      Left            =   8040
      TabIndex        =   124
      Top             =   4560
      Width           =   960
   End
   Begin VB.Label lblJNTaccUnit 
      Caption         =   "milliseconds."
      Height          =   330
      Index           =   0
      Left            =   8040
      TabIndex        =   123
      Top             =   4200
      Width           =   960
   End
   Begin VB.Label lblJNTAccTime 
      Alignment       =   1  'Right Justify
      BorderStyle     =   1  'Fixed Single
      Caption         =   "600"
      Height          =   285
      Index           =   3
      Left            =   7200
      TabIndex        =   122
      Top             =   4920
      Width           =   735
   End
   Begin VB.Label lblJNTAccTime 
      Alignment       =   1  'Right Justify
      BorderStyle     =   1  'Fixed Single
      Caption         =   "600"
      Height          =   285
      Index           =   2
      Left            =   6360
      TabIndex        =   121
      Top             =   4920
      Width           =   735
   End
   Begin VB.Label lblCNT 
      Alignment       =   1  'Right Justify
      Caption         =   "CNT"
      BeginProperty Font 
         Name            =   "Comic Sans MS"
         Size            =   8.25
         Charset         =   0
         Weight          =   700
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   285
      Index           =   7
      Left            =   9120
      TabIndex        =   105
      Top             =   3120
      Width           =   375
   End
   Begin VB.Label lblCNT 
      Alignment       =   1  'Right Justify
      Caption         =   "CNT"
      BeginProperty Font 
         Name            =   "Comic Sans MS"
         Size            =   8.25
         Charset         =   0
         Weight          =   700
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   285
      Index           =   6
      Left            =   9120
      TabIndex        =   104
      Top             =   2760
      Width           =   375
   End
   Begin VB.Label lblCNT 
      Alignment       =   1  'Right Justify
      Caption         =   "CNT"
      BeginProperty Font 
         Name            =   "Comic Sans MS"
         Size            =   8.25
         Charset         =   0
         Weight          =   700
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   285
      Index           =   5
      Left            =   9120
      TabIndex        =   103
      Top             =   2400
      Width           =   375
   End
   Begin VB.Label lblCNT 
      Alignment       =   1  'Right Justify
      Caption         =   "CNT"
      BeginProperty Font 
         Name            =   "Comic Sans MS"
         Size            =   8.25
         Charset         =   0
         Weight          =   700
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   285
      Index           =   4
      Left            =   9120
      TabIndex        =   102
      Top             =   2040
      Width           =   375
   End
   Begin VB.Label lblCNT 
      Alignment       =   1  'Right Justify
      Caption         =   "CNT"
      BeginProperty Font 
         Name            =   "Comic Sans MS"
         Size            =   8.25
         Charset         =   0
         Weight          =   700
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   285
      Index           =   3
      Left            =   9120
      TabIndex        =   101
      Top             =   1680
      Width           =   375
   End
   Begin VB.Label lblCNT 
      Alignment       =   1  'Right Justify
      Caption         =   "CNT"
      BeginProperty Font 
         Name            =   "Comic Sans MS"
         Size            =   8.25
         Charset         =   0
         Weight          =   700
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   285
      Index           =   2
      Left            =   9120
      TabIndex        =   100
      Top             =   1320
      Width           =   375
   End
   Begin VB.Label lblCNT 
      Alignment       =   1  'Right Justify
      Caption         =   "CNT"
      BeginProperty Font 
         Name            =   "Comic Sans MS"
         Size            =   8.25
         Charset         =   0
         Weight          =   700
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   285
      Index           =   1
      Left            =   9120
      TabIndex        =   99
      Top             =   960
      Width           =   375
   End
   Begin VB.Label lblCNT 
      Alignment       =   1  'Right Justify
      Caption         =   "CNT"
      BeginProperty Font 
         Name            =   "Comic Sans MS"
         Size            =   8.25
         Charset         =   0
         Weight          =   700
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   285
      Index           =   8
      Left            =   9120
      TabIndex        =   98
      Top             =   3480
      Width           =   375
   End
   Begin VB.Label Label5 
      Alignment       =   2  'Center
      Caption         =   "CNT(0-100)"
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
      Left            =   8880
      TabIndex        =   97
      Top             =   360
      Width           =   1215
   End
   Begin VB.Label Label4 
      Alignment       =   2  'Center
      Caption         =   "End Type"
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
      Left            =   9000
      TabIndex        =   96
      Top             =   120
      Width           =   975
   End
   Begin VB.Label lblPoint 
      Caption         =   "Point 9"
      Height          =   285
      Index           =   9
      Left            =   4800
      TabIndex        =   95
      Top             =   3840
      Width           =   615
   End
   Begin VB.Label lblJNTAccTotal 
      Alignment       =   1  'Right Justify
      Caption         =   "Total Accel"
      Height          =   240
      Left            =   4440
      TabIndex        =   94
      Top             =   4920
      Width           =   960
   End
   Begin VB.Label lblPoint 
      Caption         =   "Point 8"
      Height          =   285
      Index           =   8
      Left            =   4800
      TabIndex        =   93
      Top             =   3480
      Width           =   615
   End
   Begin VB.Label lblPoint 
      Caption         =   "Point 7"
      Height          =   285
      Index           =   7
      Left            =   4800
      TabIndex        =   92
      Top             =   3120
      Width           =   615
   End
   Begin VB.Label lblPoint 
      Caption         =   "Point 6"
      Height          =   285
      Index           =   6
      Left            =   4800
      TabIndex        =   91
      Top             =   2760
      Width           =   615
   End
   Begin VB.Label lblPoint 
      Caption         =   "Point 5"
      Height          =   285
      Index           =   5
      Left            =   4800
      TabIndex        =   90
      Top             =   2400
      Width           =   615
   End
   Begin VB.Label lblUnitsDis 
      Alignment       =   2  'Center
      Caption         =   "mm"
      BeginProperty Font 
         Name            =   "Comic Sans MS"
         Size            =   8.25
         Charset         =   0
         Weight          =   700
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   285
      Index           =   2
      Left            =   7200
      TabIndex        =   89
      Top             =   360
      Width           =   735
   End
   Begin VB.Label lblUnitsDis 
      Alignment       =   2  'Center
      Caption         =   "mm"
      BeginProperty Font 
         Name            =   "Comic Sans MS"
         Size            =   8.25
         Charset         =   0
         Weight          =   700
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   285
      Index           =   1
      Left            =   6360
      TabIndex        =   88
      Top             =   360
      Width           =   735
   End
   Begin VB.Label lblSpeed 
      Alignment       =   2  'Center
      Caption         =   "Speed"
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
      Left            =   7920
      TabIndex        =   87
      Top             =   120
      Width           =   975
   End
   Begin VB.Label lblZ_J3 
      Alignment       =   2  'Center
      Caption         =   "Z"
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
      Left            =   7200
      TabIndex        =   86
      Top             =   120
      Width           =   735
   End
   Begin VB.Label lblY_J2 
      Alignment       =   2  'Center
      Caption         =   "Y"
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
      Left            =   6360
      TabIndex        =   85
      Top             =   120
      Width           =   735
   End
   Begin VB.Label lblX_J1 
      Alignment       =   2  'Center
      Caption         =   "X"
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
      Left            =   5520
      TabIndex        =   84
      Top             =   120
      Width           =   735
   End
   Begin VB.Label lblPoint 
      Caption         =   "Point 4"
      Height          =   285
      Index           =   4
      Left            =   4800
      TabIndex        =   83
      Top             =   2040
      Width           =   615
   End
   Begin VB.Label lblPoint 
      Caption         =   "Point 3"
      Height          =   285
      Index           =   3
      Left            =   4800
      TabIndex        =   82
      Top             =   1680
      Width           =   615
   End
   Begin VB.Label lblPoint 
      Caption         =   "Point 2"
      Height          =   285
      Index           =   2
      Left            =   4800
      TabIndex        =   81
      Top             =   1320
      Width           =   615
   End
   Begin VB.Label lblPoint 
      Caption         =   "Point 1"
      Height          =   285
      Index           =   1
      Left            =   4800
      TabIndex        =   80
      Top             =   960
      Width           =   615
   End
   Begin VB.Label lblPoint 
      Appearance      =   0  'Flat
      BackColor       =   &H8000000A&
      Caption         =   "Point 0"
      ForeColor       =   &H80000008&
      Height          =   255
      Index           =   0
      Left            =   4800
      TabIndex        =   79
      Top             =   600
      Width           =   615
   End
   Begin VB.Label lblInfo 
      Alignment       =   2  'Center
      BackColor       =   &H00E0E0E0&
      Caption         =   "Check Status before calculating motion!"
      BeginProperty Font 
         Name            =   "Comic Sans MS"
         Size            =   15.75
         Charset         =   0
         Weight          =   400
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      ForeColor       =   &H000000FF&
      Height          =   855
      Left            =   120
      TabIndex        =   78
      Top             =   5640
      Width           =   5280
   End
   Begin VB.Label Label3 
      Caption         =   "milliseconds."
      Height          =   330
      Left            =   2280
      TabIndex        =   77
      Top             =   5220
      Width           =   960
   End
   Begin VB.Label lblOutput 
      Caption         =   "Print output every"
      Height          =   315
      Left            =   120
      TabIndex        =   76
      Top             =   5220
      Width           =   1680
   End
   Begin VB.Label lblJNTAccTime 
      Alignment       =   1  'Right Justify
      BorderStyle     =   1  'Fixed Single
      Caption         =   "600"
      Height          =   285
      Index           =   1
      Left            =   5520
      TabIndex        =   75
      Top             =   4920
      Width           =   735
   End
   Begin VB.Label lblUnitsUpdate 
      Caption         =   "milliseconds"
      Height          =   285
      Left            =   2640
      TabIndex        =   74
      Top             =   3120
      Width           =   1185
   End
   Begin VB.Label lblUpdate 
      Alignment       =   1  'Right Justify
      Caption         =   "Controller Update Rate:"
      Height          =   285
      Left            =   120
      TabIndex        =   73
      Top             =   3120
      Width           =   1860
   End
   Begin VB.Label lblJNTAcc2 
      Alignment       =   1  'Right Justify
      Caption         =   "JNTAccel 2"
      Height          =   285
      Left            =   4440
      TabIndex        =   66
      Top             =   4560
      Width           =   960
   End
   Begin VB.Label lblJNTAcc1 
      Alignment       =   1  'Right Justify
      Caption         =   "JNTAccel 1"
      Height          =   285
      Left            =   4440
      TabIndex        =   65
      Top             =   4200
      Width           =   960
   End
   Begin VB.Label lblUnitsDis 
      Alignment       =   2  'Center
      Caption         =   "mm"
      BeginProperty Font 
         Name            =   "Comic Sans MS"
         Size            =   8.25
         Charset         =   0
         Weight          =   700
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   285
      Index           =   0
      Left            =   5520
      TabIndex        =   64
      Top             =   360
      Width           =   735
   End
   Begin VB.Label lblUnitsVel 
      Alignment       =   2  'Center
      Caption         =   "mm/sec"
      BeginProperty Font 
         Name            =   "Comic Sans MS"
         Size            =   8.25
         Charset         =   0
         Weight          =   700
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   285
      Left            =   7920
      TabIndex        =   63
      Top             =   360
      Width           =   975
   End
End
Attribute VB_Name = "frmInput"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False



Private Sub cmdCalculate_Click()
        
    frmDynamics.Show
    Screen.MousePointer = vbDefault
      
End Sub

Private Sub cmdExit_Click()
    Unload Me
    Unload frmDynamics
    
End Sub

Private Sub cmdStatus_Click()
           
    Dim I As Integer
    Dim MagDis(1 To 9) As Double
    
    
    If Val(txtUpdate.Text) < 4 Then
        lblInfo.Caption = "Update Rate must be >= 4 msec for RJ-3!"
        Exit Sub
    End If
    
    If Val(txtExp.Text) <= 0 And txtExp.Enabled = True Then
        lblInfo.Caption = "Decay Time must be > 0."
        Exit Sub
    End If
    
    For I = 1 To 9
    
        'All path speeds must positive and nonzero
        If Val(txtSpeed(I).Text) <= 0 And txtSpeed(I).Enabled Then
            lblInfo.Caption = "Speed must be > 0."
            Exit Sub
        End If
    
        'check magnitude of displacement vector, must be >0
        MagDis(I) = Sqr((Val(txtPx(I).Text) - Val(txtPx(I - 1).Text)) ^ 2 _
                     + (Val(txtPy(I).Text) - Val(txtPy(I - 1).Text)) ^ 2 _
                     + (Val(txtPz(I).Text) - Val(txtPz(I - 1).Text)) ^ 2)
                     
        If MagDis(I) <= 0 And txtPx(I).Enabled = True Then
            lblInfo.Caption = "Distance from point " & I - 1 & _
             " to " & I & " must be > 0."
            Exit Sub
        End If
    Next I
    
    If Val(txtOutput.Text) <= 0 Then
        lblInfo.Caption = "Output step must be > 0."
        Exit Sub
    End If
    
    'Only if there are no errors will these be calculated.
     lblInfo.Caption = "Path Validated. Motion can now be calculated!"
    
    cmdCalculate.Enabled = True
    
End Sub

Private Sub Form_Load() 'complete 8/10/2000
    
    Dim I As Integer
    
    'Disable the exponential filter time entry locations
    lblExp.Enabled = False
    txtExp.Enabled = False
    txtExp.BackColor = vbActiveBorder
    lblUnitsExp.Enabled = False
    
    'Disable the <Calculate Motion> button
    cmdCalculate.Enabled = False
    
    'Set the number of path points to 2.
    optNumPoints(2).Value = True
    
    'Disable point input for points 2-9.
    For I = 2 To 9
        lblPoint(I).Enabled = False
        txtPx(I).Enabled = False
        txtPy(I).Enabled = False
        txtPz(I).Enabled = False
        txtPx(I).BackColor = vbActiveBorder
        txtPy(I).BackColor = vbActiveBorder
        txtPz(I).BackColor = vbActiveBorder
        
        'disable speed input for points 2-9.
        txtSpeed(I).Enabled = False
        txtSpeed(I).BackColor = vbActiveBorder
        lblPercent(I).Enabled = False
        
        'disable the end condition from 1 to 8.
        lblCNT(I - 1).Enabled = False
        txtEnd(I - 1).Enabled = False
        txtEnd(I - 1).BackColor = vbActiveBorder
    Next I
    
    'Disable the joint speed & acceleration boxes
    lblJNTAcc1.Enabled = False
    lblJNTAcc2.Enabled = False
    lblJNTAccTotal.Enabled = False
    lblJNTSpeed.Enabled = False
    lblJNTSpeedUnits.Enabled = False
    For I = 1 To 3
        txtJNTAcc1(I).Enabled = False
        txtJNTAcc1(I).BackColor = vbActiveBorder
        txtJNTAcc2(I).Enabled = False
        txtJNTAcc2(I).BackColor = vbActiveBorder
        txtJNTSpeed(I).Enabled = False
        txtJNTSpeed(I).BackColor = vbActiveBorder
        lblJNTAccTime(I).Enabled = False
        lblJNTaccUnit(I - 1).Enabled = False
    Next I
    
    
    
    
End Sub



Private Sub optDblLinear_Click() 'complete 8/10/2000

    'Disable the exponential filter time entry locations
    lblExp.Enabled = False
    txtExp.Enabled = False
    txtExp.BackColor = vbActiveBorder
    lblUnitsExp.Enabled = False
    
End Sub

Private Sub optExp_Click() 'complete 8/10/2000

    'Enable the exponential filter time entry locations
    lblExp.Enabled = True
    txtExp.Enabled = True
    txtExp.BackColor = vbWindowBackground
    lblUnitsExp.Enabled = True
    
    cmdCalculate.Enabled = False
    lblInfo.Caption = "Check status before calculating motion!"
End Sub

Private Sub optLinear_Click() 'complete 8/10/2000
    Dim I As Integer
    
   'Change the units to linear
    lblUnitsVel.Caption = "mm/sec"
    lblX_J1.Caption = "X"
    lblY_J2.Caption = "Y"
    lblZ_J3.Caption = "Z"
    
    For I = 0 To 2
        lblUnitsDis(I).Caption = "mm"
    Next I
    
    'Disabled the joint acceleration boxes
    lblJNTAcc1.Enabled = False
    lblJNTAcc2.Enabled = False
    lblJNTAccTotal.Enabled = False
    lblJNTSpeed.Enabled = False
    lblJNTSpeedUnits.Enabled = False
    For I = 1 To 3
        txtJNTAcc1(I).Enabled = False
        txtJNTAcc1(I).BackColor = vbActiveBorder
        txtJNTAcc2(I).Enabled = False
        txtJNTAcc2(I).BackColor = vbActiveBorder
        txtJNTSpeed(I).Enabled = False
        txtJNTSpeed(I).BackColor = vbActiveBorder
        lblJNTAccTime(I).Enabled = False
        lblJNTaccUnit(I - 1).Enabled = False
        
        'Enable the linear acceleration units
        lblCartAccUnit(I - 1).Enabled = True
    Next I
    
    'Change initial speed values to 1500.
    For I = 1 To 9
        txtSpeed(I).Text = "1500"
        lblPercent(I).Visible = False
    Next I
    
    'Turn on the linear acceleration time inputs
    frameCart.Enabled = True
    lblCartAcc1.Enabled = True
    lblCartAcc2.Enabled = True
    txtCartAcc1.Enabled = True
    txtCartAcc1.BackColor = vbWindowBackground
    txtCartAcc2.Enabled = True
    txtCartAcc2.BackColor = vbWindowBackground
    lblCartAccTotal.Enabled = True
    txtCartAccTotal.Enabled = True
    
End Sub

Private Sub optNumPoints_Click(Index As Integer)
    
    Dim I As Integer
    
    'Set the calculate button to disabled
    cmdCalculate.Enabled = False
    
    'Enable all points up to number of points (remember 0 shift).
    For I = 0 To Index - 1
        lblPoint(I).Enabled = True
        txtPx(I).Enabled = True
        txtPy(I).Enabled = True
        txtPz(I).Enabled = True
        txtPx(I).BackColor = vbWindowBackground
        txtPy(I).BackColor = vbWindowBackground
        txtPz(I).BackColor = vbWindowBackground
        
        If I <> 0 Then
            'enable speed input for points.
            txtSpeed(I).Enabled = True
            txtSpeed(I).BackColor = vbWindowBackground
            lblPercent(I).Enabled = True
        End If
        
        If I > 1 Then
            'disable the end condition.
            lblCNT(I - 1).Enabled = True
            txtEnd(I - 1).Enabled = True
            txtEnd(I - 1).BackColor = vbWindowBackground
        End If
        
    Next I
    
    If Index < 10 Then
    'Disable all points after number of points (remember 0 shift).
        For I = Index To 9
            lblPoint(I).Enabled = False
            txtPx(I).Enabled = False
            txtPy(I).Enabled = False
            txtPz(I).Enabled = False
            txtPx(I).BackColor = vbActiveBorder
            txtPy(I).BackColor = vbActiveBorder
            txtPz(I).BackColor = vbActiveBorder
        
            'disable speed input for points 2-9.
            txtSpeed(I).Enabled = False
            txtSpeed(I).BackColor = vbActiveBorder
            lblPercent(I).Enabled = False
        
            'disable the end condition from 1 to 8.
            lblCNT(I - 1).Enabled = False
            txtEnd(I - 1).Enabled = False
            txtEnd(I - 1).BackColor = vbActiveBorder
        Next I
    End If
    
End Sub

Private Sub optRotate_Click() 'complete 8/10/2000
    Dim I As Integer
    
    'Change the units to rotational
    lblUnitsVel.Caption = "% Joint"
    lblX_J1.Caption = "J1"
    lblY_J2.Caption = "J2"
    lblZ_J3.Caption = "J3"
    
    For I = 0 To 2
        lblUnitsDis(I).Caption = "deg"
    Next I
    
    'Enable the joint acceleration boxes
    lblJNTAcc1.Enabled = True
    lblJNTAcc2.Enabled = True
    lblJNTAccTotal.Enabled = True
    lblJNTSpeed.Enabled = True
    lblJNTSpeedUnits.Enabled = True
    For I = 1 To 3
        txtJNTAcc1(I).Enabled = True
        txtJNTAcc1(I).BackColor = vbWindowBackground
        txtJNTAcc2(I).Enabled = True
        txtJNTAcc2(I).BackColor = vbWindowBackground
        txtJNTSpeed(I).Enabled = True
        txtJNTSpeed(I).BackColor = vbWindowBackground
        lblJNTAccTime(I).Enabled = True
        lblJNTaccUnit(I - 1).Enabled = True
        
        'Disable the linear acceleration units
        lblCartAccUnit(I - 1).Enabled = False
    Next I
    
    'Change the initial speed value to 100%
    For I = 1 To 9
        txtSpeed(I).Text = "100"
        lblPercent(I).Visible = True
    Next I
    
    'Disable the linear acceleration time inputs
    frameCart.Enabled = False
    lblCartAcc1.Enabled = False
    lblCartAcc2.Enabled = False
    txtCartAcc1.Enabled = False
    txtCartAcc1.BackColor = vbActiveBorder
    txtCartAcc2.Enabled = False
    txtCartAcc2.BackColor = vbActiveBorder
    lblCartAccTotal.Enabled = False
    txtCartAccTotal.Enabled = False
    
End Sub








Private Sub txtcartacc1_Change() 'Complete on 8/10/2000
    If Val(txtCartAcc2.Text) >= 0 And Val(txtCartAcc1.Text) >= 0 Then
        
        txtCartAccTotal.Caption = Val(txtCartAcc1.Text) + Val(txtCartAcc2.Text)
        
    End If
    
    cmdCalculate.Enabled = False
    lblInfo.Caption = "Check status before calculating motion!"
End Sub

Private Sub txtCartAcc1_GotFocus()
    txtCartAcc1.SelStart = 0
    ' Highlight to end of text.
    txtCartAcc1.SelLength = Len(txtCartAcc1.Text)
End Sub

Private Sub txtcartacc1_KeyPress(KeyAscii As Integer) 'Complete on 8/10/2000
    If KeyAscii <> 8 Then 'Allows backspace
        If Chr(KeyAscii) < "0" Or Chr(KeyAscii) > "9" Then 'Must type in an integer
            KeyAscii = 0
            Beep
        End If
    End If
    
End Sub



Private Sub txtCartAcc1_LostFocus()
    If Val(txtCartAcc1.Text) <= 0 Then
        MsgBox "CartAccel 1 must be > 0!", vbExclamation, "Acceleration Error"
        txtCartAcc1.SetFocus
    End If
            
End Sub

Private Sub txtcartacc2_Change()
    
    If Val(txtCartAcc2.Text) >= 0 And Val(txtCartAcc1.Text) >= 0 Then
        txtCartAccTotal.Caption = Val(txtCartAcc1.Text) + Val(txtCartAcc2.Text)
                
    End If
    
    cmdCalculate.Enabled = False
    lblInfo.Caption = "Check status before calculating motion!"
End Sub

Private Sub txtCartAcc2_GotFocus()
    txtCartAcc2.SelStart = 0
    ' Highlight to end of text.
    txtCartAcc2.SelLength = Len(txtCartAcc2.Text)
End Sub

Private Sub txtcartacc2_KeyPress(KeyAscii As Integer)
    If KeyAscii <> 8 Then 'Allows backspace
        If Chr(KeyAscii) < "0" Or Chr(KeyAscii) > "9" Then 'Must type in an integer
            KeyAscii = 0
            Beep
        End If
    End If
    
End Sub

Private Sub txtCartAcc2_LostFocus()
    If Val(txtCartAcc2.Text) <= 0 Then
        MsgBox "CartAccel 2 must be > 0!", vbExclamation, "Acceleration Error"
        txtCartAcc2.SetFocus
    End If
End Sub

Private Sub txtEnd_Change(Index As Integer)

    cmdCalculate.Enabled = False
    lblInfo.Caption = "Check status before calculating motion!"
End Sub

Private Sub txtEnd_GotFocus(Index As Integer)
    txtEnd(Index).SelStart = 0
    ' Highlight to end of text.
    txtEnd(Index).SelLength = Len(txtEnd(Index).Text)
End Sub

Private Sub txtEnd_KeyPress(Index As Integer, KeyAscii As Integer)
    If KeyAscii <> 8 Then 'Allows backspace
        If Chr(KeyAscii) < "0" Or Chr(KeyAscii) > "9" Then 'Must type in an integer
            KeyAscii = 0
            Beep
        End If
    End If
End Sub

Private Sub txtEnd_LostFocus(Index As Integer)
    If Val(txtEnd(Index).Text) < 0 Or Val(txtEnd(Index).Text) > 100 Then
        MsgBox "End condition for point" & Index & " must be from 0 - 100!", _
        vbExclamation, "End Condition Error"
        txtEnd(Index).SetFocus
    End If
    
    If txtEnd(Index).Text = "" Then
        txtEnd(Index).Text = "0"
    End If
End Sub

Private Sub txtExp_Change()
    cmdCalculate.Enabled = False
    lblInfo.Caption = "Check status before calculating motion!"
End Sub

Private Sub txtExp_GotFocus()
    txtExp.SelStart = 0
    ' Highlight to end of text.
    txtExp.SelLength = Len(txtExp.Text)
End Sub

Private Sub txtExp_KeyPress(KeyAscii As Integer)
    If KeyAscii <> 8 Then 'Allows backspace
        If Chr(KeyAscii) < "0" Or Chr(KeyAscii) > "9" Then 'Must type in an integer
            KeyAscii = 0
            Beep
        End If
    End If
    
    
End Sub


Private Sub txtJNTAcc1_Change(Index As Integer)

    If Val(txtJNTAcc2(Index).Text) >= 0 And Val(txtJNTAcc1(Index).Text) >= 0 Then
        
        lblJNTAccTime(Index).Caption = Val(txtJNTAcc1(Index).Text) _
                                     + Val(txtJNTAcc2(Index).Text)
        
    End If
    
    cmdCalculate.Enabled = False
    lblInfo.Caption = "Check status before calculating motion!"
End Sub

Private Sub txtJNTAcc1_GotFocus(Index As Integer)
    txtJNTAcc1(Index).SelStart = 0
    ' Highlight to end of text.
    txtJNTAcc1(Index).SelLength = Len(txtJNTAcc1(Index).Text)
End Sub

Private Sub txtJNTAcc1_KeyPress(Index As Integer, KeyAscii As Integer)
    If KeyAscii <> 8 Then 'Allows backspace
        If Chr(KeyAscii) < "0" Or Chr(KeyAscii) > "9" Then 'Must type in an integer
            KeyAscii = 0
            Beep
        End If
    End If
End Sub

Private Sub txtJNTAcc1_LostFocus(Index As Integer)
    If Val(txtJNTAcc1(Index).Text) <= 0 Then
        MsgBox "JNTAccel 1 for J" & Index & " must be > 0!", _
        vbExclamation, "Acceleration Error"
        txtJNTAcc1(Index).SetFocus
    End If
End Sub

Private Sub txtJNTAcc2_GotFocus(Index As Integer)
    txtJNTAcc2(Index).SelStart = 0
    ' Highlight to end of text.
    txtJNTAcc2(Index).SelLength = Len(txtJNTAcc2(Index).Text)
End Sub

Private Sub txtJNTAcc2_LostFocus(Index As Integer)
    If Val(txtJNTAcc2(Index).Text) <= 0 Then
        MsgBox "JNTAccel 2 for J" & Index & " must be > 0!", _
        vbExclamation, "Acceleration Error"
        txtJNTAcc2(Index).SetFocus
    End If
End Sub
Private Sub txtJNTAcc2_KeyPress(Index As Integer, KeyAscii As Integer)
    If KeyAscii <> 8 Then 'Allows backspace
        If Chr(KeyAscii) < "0" Or Chr(KeyAscii) > "9" Then 'Must type in an integer
            KeyAscii = 0
            Beep
        End If
    End If
End Sub
Private Sub txtJNTAcc2_Change(Index As Integer)

    If Val(txtJNTAcc2(Index).Text) >= 0 And Val(txtJNTAcc1(Index).Text) >= 0 Then
        
        lblJNTAccTime(Index).Caption = Val(txtJNTAcc1(Index).Text) _
                                     + Val(txtJNTAcc2(Index).Text)
        
    End If
    
    cmdCalculate.Enabled = False
    lblInfo.Caption = "Check status before calculating motion!"
End Sub

Private Sub txtJNTSpeed_GotFocus(Index As Integer)
    txtJNTSpeed(Index).SelStart = 0
    ' Highlight to end of text.
    txtJNTSpeed(Index).SelLength = Len(txtJNTSpeed(Index).Text)
End Sub

Private Sub txtOutput_Change()
    cmdCalculate.Enabled = False
    lblInfo.Caption = "Check status before calculating motion!"
End Sub

Private Sub txtOutput_GotFocus()
    txtOutput.SelStart = 0
    ' Highlight to end of text.
    txtOutput.SelLength = Len(txtOutput.Text)
End Sub

Private Sub txtPx_Change(Index As Integer)

    cmdCalculate.Enabled = False
    lblInfo.Caption = "Check status before calculating motion!"
End Sub

Private Sub txtPx_gotfocus(Index As Integer)
    
    txtPx(Index).SelStart = 0
    ' Highlight to end of text.
    txtPx(Index).SelLength = Len(txtPx(Index).Text)
   
End Sub

Private Sub txtPy_Change(Index As Integer)

    cmdCalculate.Enabled = False
    lblInfo.Caption = "Check status before calculating motion!"
End Sub

Private Sub txtPy_GotFocus(Index As Integer)
    txtPy(Index).SelStart = 0
    ' Highlight to end of text.
    txtPy(Index).SelLength = Len(txtPy(Index).Text)
End Sub

Private Sub txtPz_Change(Index As Integer)

    cmdCalculate.Enabled = False
    lblInfo.Caption = "Check status before calculating motion!"
End Sub

Private Sub txtPz_GotFocus(Index As Integer)
    txtPz(Index).SelStart = 0
    ' Highlight to end of text.
    txtPz(Index).SelLength = Len(txtPz(Index).Text)
End Sub

Private Sub txtSpeed_Change(Index As Integer)
    cmdCalculate.Enabled = False
    lblInfo.Caption = "Check status before calculating motion!"
End Sub

Private Sub txtSpeed_GotFocus(Index As Integer)
    txtSpeed(Index).SelStart = 0
    ' Highlight to end of text.
    txtSpeed(Index).SelLength = Len(txtSpeed(Index).Text)
End Sub

Private Sub txtUpdate_Change()
    cmdCalculate.Enabled = False
    lblInfo.Caption = "Check status before calculating motion!"
End Sub

Private Sub txtUpdate_GotFocus()
    txtUpdate.SelStart = 0
    ' Highlight to end of text.
    txtUpdate.SelLength = Len(txtUpdate.Text)
End Sub

Private Sub txtUpdate_KeyPress(KeyAscii As Integer)
    If KeyAscii <> 8 Then 'Allows backspace
        If Chr(KeyAscii) < "0" Or Chr(KeyAscii) > "9" Then 'Must type in an integer
            KeyAscii = 0
            Beep
        End If
    End If
    
    
End Sub
Private Sub txtJNTspeed_Change(Index As Integer)
    
    cmdCalculate.Enabled = False
    lblInfo.Caption = "Check status before calculating motion!"
End Sub

Private Sub txtJNTspeed_KeyPress(Index As Integer, KeyAscii As Integer)
    If KeyAscii <> 8 Then 'Allows backspace &
        If Chr(KeyAscii) < "0" Or Chr(KeyAscii) > "9" Then 'Must type in an integer
            KeyAscii = 0
            Beep
        End If
    End If
End Sub

Private Sub txtJNTspeed_LostFocus(Index As Integer)
    If Val(txtJNTSpeed(Index).Text) <= 0 Then
        MsgBox "Joint speed for J" & Index & " must be > 0!", _
        vbExclamation, "Speed Error"
        txtSpeed(Index).SetFocus
    End If
End Sub

