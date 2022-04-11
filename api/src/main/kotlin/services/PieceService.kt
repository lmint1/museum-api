package services

import usecase.ListAllPieces
import usecase.VotePiece

class PieceService(val votePiece: VotePiece, val listAllPieces: ListAllPieces)